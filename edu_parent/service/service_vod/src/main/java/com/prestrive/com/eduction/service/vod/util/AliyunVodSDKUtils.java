package com.prestrive.com.eduction.service.vod.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.scheduling.support.SimpleTriggerContext;

public class AliyunVodSDKUtils {

    /**
     * AccessKey初始化
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    public static DefaultAcsClient initVodClient(String accessKeyId , String accessKeySecret){
        String regionId = "cn-shanghai";

        DefaultProfile profile = DefaultProfile.getProfile(regionId,accessKeyId, accessKeySecret);
        DefaultAcsClient defaultAcsClient = new DefaultAcsClient(profile);
        return defaultAcsClient;
    }
}
