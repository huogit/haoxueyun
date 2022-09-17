package com.prestrive.com.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.prestrive.com.service.edu.entity.Subject;
import com.prestrive.com.service.edu.entity.excel.ExcelSubjectData;
import com.prestrive.com.service.edu.entity.vo.SubjectVo;
import com.prestrive.com.service.edu.listener.ExcelSubjectDataListener;
import com.prestrive.com.service.edu.mapper.SubjectMapper;
import com.prestrive.com.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(subjectMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet()
                .doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        List<SubjectVo> subjectVos = subjectMapper.selectNestedListByParentId("0");
        return subjectVos;
    }
}
