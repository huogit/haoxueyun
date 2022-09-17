package com.prestrive.com.service.ucenter.service;

import com.prestrive.com.service.ucenter.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.ucenter.entity.vo.MessageInfoVo;

import javax.security.auth.message.MessageInfo;
import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author strive
 * @since 2022-09-14
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取消息数据
     *
     * @param id
     * @return
     */
     MessageInfoVo getMessageInfoById(Long id);

    /**
     * 保存消息数据
     * @param messageInfoVo
     * @return
     */
    String saveMessageInfo(MessageInfoVo messageInfoVo);

    /**
     * 更新消息
     * @param messageInfoVo
     * @return
     */
    boolean updateCourseInfoById(MessageInfoVo messageInfoVo);

    /**
     * 删除消息
     * @param id
     * @return
     */
    Boolean deleteMessageById(String id);

    /**
     * 获取消息数据，并更新浏览量
     *
     * @param id
     * @return
     */
    MessageInfoVo selectMessageInfoById(String id);

    /**
     * 根据id列表删除 消息
     *
     * @param idList
     * @return
     */
    void removeMessageByIds(List<String> idList);
}
