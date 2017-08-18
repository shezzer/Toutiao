package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.MessageDAO;
import com.nowcoder.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sherl on 2017/7/23.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message msg){
        return messageDAO.addMessage(msg);
    }

    public List<Message> selectByConversationId(String conversationId,int offset,int limit){
        return messageDAO.selectByConversationId(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getUnreadNum(int userId,String conversationId){
        return messageDAO.getUnreadNum(userId,conversationId);
    }
}
