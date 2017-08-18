package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.model.*;
import com.nowcoder.toutiao.service.CommentService;
import com.nowcoder.toutiao.service.NewsService;
import com.nowcoder.toutiao.service.QiniuService;
import com.nowcoder.toutiao.service.UserService;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sherl on 2017/7/19.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private NewsService newsService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QiniuService qiniuService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("title") String title,@RequestParam("link") String link,
                          @RequestParam("image") String image){
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(path = "/news/{newsId}", method = {RequestMethod.GET})
    public String newsDetail(@PathVariable(value = "newsId") int newsId, Model model){
        try{
            News news = newsService.getNews(newsId);
            if(news!=null){
                List<Comment> comments = commentService.selectByEntity(newsId, EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOS = new ArrayList<ViewObject>();
                for(Comment comment : comments){
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment",comment);
                    commentVO.set("user", userService.findUserById(comment.getUserId()));
                    commentVOS.add(commentVO);
                }
                model.addAttribute("comments", commentVOS);
            }
            model.addAttribute("news",news);
            model.addAttribute("owner",userService.findUserById(news.getUserId()));
        }catch (Exception e){
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            content = HtmlUtils.htmlEscape(content);
            Comment comment = new Comment();
            comment.setEntityId(newsId);
            comment.setContent(content);
            comment.setUserId(hostHolder.getUser().getId());
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
            newsService.updateCommentCount(comment.getEntityId(),commentService.getCommentCount(comment.getEntityId(),comment.getEntityType()));
        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
            return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(path = "/image", method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String filename, HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+filename)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片出错"+e.getMessage());
        }
    }

    @RequestMapping(path = "/uploadImage/", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl==null)
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }
}
