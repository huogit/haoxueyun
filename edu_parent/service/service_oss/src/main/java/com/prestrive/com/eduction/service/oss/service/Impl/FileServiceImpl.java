package com.prestrive.com.eduction.service.oss.service.Impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.ExceptionUtils;
import com.prestrive.com.eduction.service.oss.service.FileService;
import com.prestrive.com.eduction.service.oss.util.OssProperties;
import com.prestrive.com.service.base.exception.GlobalException;
import jdk.nashorn.internal.objects.Global;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件操作：
 * 存储文件：名字不能是中文，阿里云会自动转码，导致查询不到，也删除不掉
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    //读取配置文件（配置类）
    @Autowired
    private OssProperties ossProperties;

    //将异常往上抛，在controller抓取，处理
    @Override
    public String upload(InputStream inputStream, String module, String originalFileName) throws Exception {
        //读取配置信息
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();

        //时间文件夹
        String folder = new DateTime().toString("yyyy/MM/dd");
        //文件名
        String fileName = UUID.randomUUID().toString();
        //文件后缀，“.”后面的内容（包括.）
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = module + "/" + folder + "/" + fileName + fileExtension;

        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        // String filePath = "D:\\localpath\\examplefile.txt";


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //判断是否存在bucket
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
            //配置权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        //InputStream inputStream = new FileInputStream(filePath);
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        ossClient.shutdown();

//        //在这里抓取 异常，不抛出异常的话，会返回成功
//        try {
//            //InputStream inputStream = new FileInputStream(filePath);
//            // 创建PutObject请求。
//            ossClient.putObject(bucketName, objectName, inputStream);
//        } catch (OSSException oe) {
//            log.error(ExceptionUtils.getMessage(oe));
//            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_ERROR);
//        } catch (ClientException ce) {
//            log.error(ExceptionUtils.getMessage(ce));
//            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_ERROR);
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }

        //将文件的访问地址返回回去
        return "https://"+ bucketName +"."+  endpoint + "/" + objectName;
    }

    @Override
    public void removeFile(String url) {
        //读取配置信息
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //从url取出object
        String host = "https://"+ bucketName +"."+  endpoint + "/" ;

        //从host最后开始 切割字符串
        String objectName = url.substring(host.length());
        System.out.println(objectName);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
//        ossClient.deleteObject(bucketName,objectName);
//        ossClient.shutdown();
    }

}
