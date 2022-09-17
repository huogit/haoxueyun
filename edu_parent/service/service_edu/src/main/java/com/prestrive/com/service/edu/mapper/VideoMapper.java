package com.prestrive.com.service.edu.mapper;

import com.prestrive.com.service.edu.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Mapper
@Repository
public interface VideoMapper extends BaseMapper<Video> {

}
