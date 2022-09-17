package com.prestrive.com.service.edu.service.impl;

import com.prestrive.com.service.edu.entity.Comment;
import com.prestrive.com.service.edu.mapper.CommentMapper;
import com.prestrive.com.service.edu.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
