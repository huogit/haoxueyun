package com.prestrive.com.eduction.service.vod.service;


import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;

public interface VideoService {
    String uploadVideo(InputStream file, String originalFilename);

    void removeVideoById(String videoId) throws ClientException;

    void removeVideoByVideoIdList(List<String> videoIdList) throws ClientException;

    String getPlayAuth(String videoSourceId) throws ClientException;
}
