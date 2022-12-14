package com.prestrive.com.service.edu.mapper;

import com.prestrive.com.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Mapper
@Repository
public interface ChapterMapper extends BaseMapper<Chapter> {

}
