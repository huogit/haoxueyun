package com.prestrive.com.service.ucenter.service.impl;

import com.prestrive.com.service.ucenter.entity.Message;
import com.prestrive.com.service.ucenter.entity.MessageDescription;
import com.prestrive.com.service.ucenter.entity.vo.MessageInfoVo;
import com.prestrive.com.service.ucenter.mapper.MessageMapper;
import com.prestrive.com.service.ucenter.service.MessageDescriptionService;
import com.prestrive.com.service.ucenter.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.security.auth.message.MessageInfo;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-09-14
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    MessageDescriptionService messageDescriptionService;

    @Override
    public MessageInfoVo getMessageInfoById(Long id) {
        MessageInfoVo messageInfoVo = new MessageInfoVo();
        //获取 消息数据和消息详情
        Message message = this.getById(id);
        MessageDescription messageDescription = messageDescriptionService.getById(id);

        //构建返回对象
        if (message != null && messageDescription !=null) {
            messageInfoVo.setId(message.getId());
            messageInfoVo.setTitle(message.getTitle());
            messageInfoVo.setDescription(messageDescription.getDescription());
            messageInfoVo.setViewCount(message.getViewCount());
        }

        return messageInfoVo;
    }

    @Override
    public String saveMessageInfo(MessageInfoVo messageInfoVo) {

        Message message = new Message();
        message.setViewCount(1L);
        message.setTitle(messageInfoVo.getTitle());

        this.save(message);

        MessageDescription messageDescription = new MessageDescription();
        String description = messageInfoVo.getDescription();
        messageDescription.setDescription(description);
        messageDescriptionService.save(messageDescription);

        return message.getId();
    }

    @Override
    public boolean updateCourseInfoById(MessageInfoVo messageInfoVo) {
        Message message = new Message();
        message.setTitle(messageInfoVo.getTitle());

        boolean i = this.updateById(message);

        MessageDescription messageDescription = new MessageDescription();
        String description = messageInfoVo.getDescription();
        messageDescription.setDescription(description);
        boolean l = messageDescriptionService.updateById(messageDescription);

        return i || l;
    }

    @Override
    public Boolean deleteMessageById(String id) {
        boolean b = this.removeById(id);

        boolean b1 = messageDescriptionService.removeById(id);

        return b && b1;
    }

    @Override
    public MessageInfoVo selectMessageInfoById(String id) {
        MessageInfoVo messageInfoVo = new MessageInfoVo();
        //获取 消息数据和消息详情
        Message message = this.getById(id);
        if(StringUtils.hasLength(message.getId())){
            //更新浏览量
            message.setViewCount(message.getViewCount()+1);
            this.updateById(message);
            //构建返回对象
            messageInfoVo.setId(message.getId());
            messageInfoVo.setTitle(message.getTitle());
            messageInfoVo.setViewCount(message.getViewCount());
            messageInfoVo.setGmtCreate(message.getGmtCreate());
        }
        MessageDescription messageDescription = messageDescriptionService.getById(id);
        if (messageDescription !=null) {

            messageInfoVo.setDescription(messageDescription.getDescription());
        }
        return messageInfoVo;
    }

    @Override
    public void removeMessageByIds(List<String> idList) {
        this.removeByIds(idList);

        messageDescriptionService.removeByIds(idList);
    }
}
