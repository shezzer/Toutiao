package com.nowcoder.toutiao;

import com.nowcoder.toutiao.dao.CommentDAO;
import com.nowcoder.toutiao.dao.LoginTicketDAO;
import com.nowcoder.toutiao.dao.NewsDAO;
import com.nowcoder.toutiao.dao.UserDAO;
import com.nowcoder.toutiao.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDataBaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
	NewsDAO newsDAO;
	@Autowired
	LoginTicketDAO loginTicketDAO;
	@Autowired
	CommentDAO commentDAO;
	@Test
	public void contextLoads() {

		for(int i = 0;i < 10;i++){
			User user = new User();
			user.setName(String.format("user%d",i));
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
			userDAO.addUser(user);
		}
		for(int i = 0;i < 10;i++){
			News news = new News();
			news.setTitle(String.format("title%d",i));
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime()+1000*3600*i*5);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
			news.setLikeCount(i+1);
			news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
			news.setUserId(i+1);
			newsDAO.addNews(news);

			LoginTicket ticket = new LoginTicket();
			ticket.setUserId(i+1);
			ticket.setExpired(date);
			ticket.setStatus(0);
			ticket.setTicket(String.format("TICKET%d",i+1));
			loginTicketDAO.addTicket(ticket);

			for(int j = 0;j < 3;j++){
				Comment comment = new Comment();
				comment.setContent(String.valueOf(i));
				comment.setCreatedDate(new Date());
				comment.setEntityId(news.getId());
				comment.setUserId(i+1);
				comment.setStatus(0);
				comment.setEntityType(EntityType.ENTITY_NEWS);
				commentDAO.addComment(comment);
			}
		}
	}

}
