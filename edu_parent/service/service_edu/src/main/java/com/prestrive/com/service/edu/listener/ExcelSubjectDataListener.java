package com.prestrive.com.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.edu.entity.Subject;
import com.prestrive.com.service.edu.entity.excel.ExcelSubjectData;
import com.prestrive.com.service.edu.mapper.SubjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
public class ExcelSubjectDataListener implements ReadListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        exception.printStackTrace();
        throw new GlobalException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {

    }

    /**
     *每一条数据解析都会来调用
     *如何填充：根据@ExcelProperty("一级分类")来填充
     * @param excelSubjectData
     * @param context
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext context) {

        log.info("解析到一条数据:{}", excelSubjectData);
        Subject subject = new Subject();
        //一级分类的id
        String parentId = null;
        //持久化一级级标题
        String levelOneTitle = excelSubjectData.getLevelOneTitle();
        Subject oneSubject = this.getByTile(levelOneTitle);
        //判断一级分类是否存在
        if(oneSubject == null){
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            subjectMapper.insert(subject);
            parentId = subject.getId();
        }else {
            parentId = oneSubject.getId();
        }

        subject = new Subject(); //null没有数据类型，Subject 是一个数据类型，两者不一样

        //持久化二级标题
        String levelTowTitle = excelSubjectData.getLevelTowTitle();
        Subject towSubByTitle = this.getTowSubByTitle(levelTowTitle, parentId);
        //判断二级分类是否存在
        if(towSubByTitle == null){
            subject.setTitle(levelTowTitle);
            subject.setParentId(parentId);
            //将二级分类存入数据库
            subjectMapper.insert(subject);
        }
        log.info("一级分类:{}", levelOneTitle);
        log.info("二级分类:{}", levelTowTitle);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("最后统一保存到数据库");
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return true ;
    }

    //确保一级分类和二级分类关联是唯一的
    /**
     *  查询是否存在一级标题
     * @param title
     * @return
     */
    private Subject getByTile(String title){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("title",title);
        subjectQueryWrapper.eq("parent_id","0");

        Subject onesubject = subjectMapper.selectOne(subjectQueryWrapper);

        return onesubject;
    }

    /**
     *  确保一级分类下的二级分类是唯一的
     * @param title
     * @return
     */
    private Subject getTowSubByTitle(String title,String  parentId){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("title",title);
        subjectQueryWrapper.eq("parent_id",parentId);

        return subjectMapper.selectOne(subjectQueryWrapper);
    }


}
