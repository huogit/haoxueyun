package com.prestrive.com.service.edu.service;

import com.prestrive.com.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
public interface ChapterService extends IService<Chapter> {

    boolean removeChapterById(String id);

    List<ChapterVo> findChapterListByCourseId(String courseId);
}
