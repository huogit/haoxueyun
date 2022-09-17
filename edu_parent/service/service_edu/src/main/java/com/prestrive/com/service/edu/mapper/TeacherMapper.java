package com.prestrive.com.service.edu.mapper;

import com.prestrive.com.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Mapper
@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {

}
