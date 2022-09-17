package com.prestrive.com.common.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


@Component
public class RedisValueOperationsUtil {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * key: "video_view_count_" + day 比如  video_view_count_20201131
     */

    public Integer get_video_view_count_day(String day){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer viewCount = (Integer) valueOperations.get("video_view_count_" + day);

        return viewCount;
    }
    public void set_video_view_count_day(String day,Integer value){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("video_view_count_" + day , value);
    }
    public void increment_video_view_count_day(String day){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.increment("video_view_count_" + day);
    }

    public Boolean  hasKey_video_view_count_day(String day){
        return redisTemplate.hasKey("video_view_count_" + day);
    }
}
