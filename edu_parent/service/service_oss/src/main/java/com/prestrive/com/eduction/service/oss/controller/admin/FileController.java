package com.prestrive.com.eduction.service.oss.controller.admin;

import ch.qos.logback.core.util.TimeUtil;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.ExceptionUtils;
import com.prestrive.com.eduction.service.oss.service.FileService;
import com.prestrive.com.service.base.exception.GlobalException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(description = "阿里云文件管理")
@RestController
//@CrossOrigin
@RequestMapping("/admin/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file")MultipartFile file,

            @ApiParam(value = "模块", required = true)
            @RequestParam("module")String module)  { //throws IOException io异常用自定义异常 处理

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            //返回访问的url地址
            String uploadUrl = fileService.upload(inputStream,module,originalFilename);
            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (Exception e) {

            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/remove")
    public R removeFile(
            @ApiParam(value = "文件的url",required = true)
            @RequestBody String url){
        fileService.removeFile(url);
        return R.ok().message("文件刪除成功");
    }

    @ApiOperation(value="test")
    @GetMapping("/test")
    public  R test(){
        log.info("oss test 被调用");
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }
}
