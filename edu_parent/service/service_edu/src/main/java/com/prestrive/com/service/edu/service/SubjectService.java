package com.prestrive.com.service.edu.service;

import com.prestrive.com.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.edu.entity.vo.SubjectVo;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public interface SubjectService extends IService<Subject> {
    public void batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();
}
