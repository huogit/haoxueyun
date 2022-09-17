package com.prestrive.com.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.edu.entity.Chapter;
import com.prestrive.com.service.edu.entity.Video;
import com.prestrive.com.service.edu.entity.vo.ChapterVo;
import com.prestrive.com.service.edu.entity.vo.VideoVo;
import com.prestrive.com.service.edu.mapper.ChapterMapper;
import com.prestrive.com.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prestrive.com.service.edu.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    VideoService videoService;
    @Autowired
    ChapterMapper chapterMapper;

    @Override
    public boolean removeChapterById(String id) {
        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",id);
        videoService.remove(videoQueryWrapper);

        //章节信息：chapter
        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> findChapterListByCourseId(String courseId) {
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        chapterQueryWrapper.orderByAsc("id","sort");
        List<Chapter> chapters = chapterMapper.selectList(chapterQueryWrapper);

        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        chapterQueryWrapper.orderByAsc("id","sort");
        List<Video> videos = videoService.list(videoQueryWrapper);

        //填充列表数据：ChapterVo
        ArrayList<ChapterVo> chapterVos = new ArrayList<>();
        for (Chapter chapter : chapters) {
            //填充chapterVo的基本属性
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //填充videoVos
            List<VideoVo> videoVos = new ArrayList<>();
            for (Video video : videos) {
                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVos.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVos);
            //添加到返回的列表中
            chapterVos.add(chapterVo);
        }
        return chapterVos;
    }
}
