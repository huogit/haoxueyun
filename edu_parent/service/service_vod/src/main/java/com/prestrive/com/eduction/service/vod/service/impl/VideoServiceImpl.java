package com.prestrive.com.eduction.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.eduction.service.vod.service.VideoService;
import com.prestrive.com.eduction.service.vod.util.AliyunVodSDKUtils;
import com.prestrive.com.eduction.service.vod.util.VodProperties;
import com.prestrive.com.service.base.exception.GlobalException;
import com.sun.org.apache.xpath.internal.objects.XString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VideoServiceImpl  implements VideoService {

    @Autowired
    VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream file, String originalFilename) {
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(vodProperties.getKeyid()
                , vodProperties.getKeysecret()
                , title, originalFilename, file);
        /* 模板组ID(可选) */
        //request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        /* 工作流ID(可选) */
        //request.setWorkflowId(vodProperties.getWorkflowId());

        UploadVideoImpl uploadVideo = new UploadVideoImpl();
        UploadStreamResponse response = uploadVideo.uploadStream(request);

        String videoId = response.getVideoId();
        //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
        //没有正确的返回videoid则说明上传失败
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败：" + response.getCode() + " - " + response.getMessage());
            throw new GlobalException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    @Override
    public void removeVideoById(String videoId) throws ClientException {
        //初始客户端
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        //配置请求
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);

        //发起删除请求，得到响应
        DeleteVideoResponse response = client.getAcsResponse(request);

        System.out.print("RequestId = " + response.getRequestId() + "\n");

    }

    @Override
    public void removeVideoByVideoIdList(List<String> videoIdList) throws ClientException {
        //初始客户端
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = videoIdList.size();
        StringBuffer idListStr = new StringBuffer();

        for (int i = 0; i < size; i++) {
            idListStr.append(videoIdList.get(i));

            if(i == size -1 || i%20 == 19){
                request.setVideoIds(idListStr.toString());
                //支持传入多个视频ID，多个用逗号分隔，最多20个
                DeleteVideoResponse response = client.getAcsResponse(request);
                //System.out.print("RequestId = " + response.getRequestId() + "\n");
                idListStr = new StringBuffer();//初始化，进行下一次删除
            }else if(i%20 < 19) {
                idListStr.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {

        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        //创建请求对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);

        //获取响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        return response.getPlayAuth();
    }

    public static void main(String[] args) {
        int size = 80;

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 1; i < size; i++) {
            stringBuffer.append(i);
            if(i%20 == 0){

                //执行删除
                System.out.println(stringBuffer.toString());

                stringBuffer = new StringBuffer();
            }else if (i % 20 > 0){
                stringBuffer.append(",");
                System.out.println(","+i + "," + (i % 20));
            }
        }
    }
}
