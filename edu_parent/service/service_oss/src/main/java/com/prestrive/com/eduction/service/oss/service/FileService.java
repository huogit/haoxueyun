package com.prestrive.com.eduction.service.oss.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface FileService {

    /**
     * 阿里云oss文件上传
     * @param inputStream 输入流
     * @param module 文件夹名称
     * @param originalFileName 原始文件名
     * @return 文件在oss服务器的url地址
     */
    public String upload(InputStream inputStream,String module,String originalFileName) throws  Exception;

    void removeFile(String url);
}
