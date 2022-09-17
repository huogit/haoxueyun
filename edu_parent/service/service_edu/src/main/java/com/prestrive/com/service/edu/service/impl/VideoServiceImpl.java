package com.prestrive.com.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.edu.entity.Video;
import com.prestrive.com.service.edu.feign.VodMediaService;
import com.prestrive.com.service.edu.mapper.VideoMapper;
import com.prestrive.com.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    VodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {
        // log.warn("VideoServiceImpl：video id = " + id);

        //从数据库找到改视频的数据
        Video video = videoMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        List<String> videoSourceIdList = new ArrayList<>();
        videoSourceIdList.add(videoSourceId);

        //log.warn("VideoServiceImpl：videoSourceId = " + videoSourceId);

        //远程调用视频删除接口
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id", chapterId);

        List<Map<String, Object>> maps = videoMapper.selectMaps(queryWrapper);

        List<String> videoSourceIds = this.getVideoSourceIds(maps);
        vodMediaService.removeVideoByIdList(videoSourceIds);
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.select("video_source_id");
        videoQueryWrapper.eq("course_id",courseId);

        List<Map<String, Object>> videoSourceIdMaps = videoMapper.selectMaps(videoQueryWrapper);

        List<String> videoSourceIds = this.getVideoSourceIds(videoSourceIdMaps);
        vodMediaService.removeVideoByIdList(videoSourceIds);
    }

    /**
     * 辅助方法:获取阿里云视频id列表
     * @param videoSourceIdMaps
     * @return
     */
    private List<String> getVideoSourceIds(List<Map<String, Object>> videoSourceIdMaps){
        ArrayList<String> videoSourceIds = new ArrayList<>();
        for (Map<String, Object> videoSourceIdMap : videoSourceIdMaps) {
            String videoSourceId = (String) videoSourceIdMap.get("video_source_id");
            videoSourceIds.add(videoSourceId);
        }
        return videoSourceIds;
    }

}
