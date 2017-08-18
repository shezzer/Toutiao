package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.CommentDAO;
import com.nowcoder.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sherl on 2017/7/21.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }

    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public List<Comment> selectByEntity(int entityId,int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }
}
