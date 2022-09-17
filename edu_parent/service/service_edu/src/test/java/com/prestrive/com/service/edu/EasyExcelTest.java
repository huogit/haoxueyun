package com.prestrive.com.service.edu;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.prestrive.com.service.edu.entity.excel.ExcelSubjectData;
import com.prestrive.com.service.edu.listener.ExcelSubjectDataListener;
import com.prestrive.com.service.edu.mapper.SubjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EasyExcelTest {

    @Autowired
    private SubjectMapper subjectMapper;

    @Test
    public void ExcelSubjectReadTest(){
        String fileName = "C:/Users/86188/Desktop/excel_write/课程分类列表模板.xls";

        System.out.printf(subjectMapper.toString());
        EasyExcel.read(fileName, ExcelSubjectData.class ,new ExcelSubjectDataListener(subjectMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet()
                .doRead();
    }
}
