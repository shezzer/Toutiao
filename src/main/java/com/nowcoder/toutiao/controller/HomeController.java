package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.model.EntityType;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.model.ViewObject;
import com.nowcoder.toutiao.service.LikeService;
import com.nowcoder.toutiao.service.NewsService;
import com.nowcoder.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherl on 2017/7/16.
 */
@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private List<ViewObject> showNewsList(int userId,int offset,int limit){
        List<News> newsList = newsService.findNewsList(userId,offset,limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.findUserById(news.getUserId()));
            if (localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId,news.getId(), EntityType.ENTITY_NEWS));
            }else
                vo.set("like",0);
            vos.add(vo);
        }
       return vos;
    }
    @RequestMapping("/")
    public String showNews(@RequestParam(value = "userId",defaultValue = "0") int userId,Model model){
        model.addAttribute("vos",showNewsList(0,0,10));
        return "home";
    }
    @RequestMapping("/user/{userId}")
    public String showUserNews(@PathVariable(value = "userId") int userId,Model model){
        model.addAttribute("vos",showNewsList(userId,0,10));
        return "home";

    }
}
