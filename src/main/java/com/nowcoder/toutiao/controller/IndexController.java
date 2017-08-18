package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.service.ToutiaoService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by Sherl on 2017/7/13.
 */
//@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ToutiaoService toutiaoService;
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(){
        return "hello nowCoder"+"toutiaoService:"+toutiaoService.say();
    }
    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
            @RequestParam(value = "type",defaultValue = "1") int type,
            @RequestParam(value = "key",defaultValue = "xx") String key){
        logger.info("visit profile");
        return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
    }
    @RequestMapping(value = "/vm")
    public String news(Model model){
        model.addAttribute("value1","string1");
        List<String> list = new ArrayList<String>();
        list.add("green");
        list.add("red");
        list.add("blue");
        model.addAttribute("list",list);
        Map<Integer,String> map = new HashedMap();
        map.put(0,"xx");
        map.put(1,"yy");
        map.put(2,"zz");
        model.addAttribute("map",map);
        model.addAttribute("user",new User("jack"));
        return "news";
    }
    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest req, HttpServletResponse res, HttpSession session)
    {
        StringBuffer sb= new StringBuffer();
        Enumeration<String> emu = req.getHeaderNames();
        while(emu.hasMoreElements()){
            sb.append(req.getHeader(emu.nextElement())+"<br>");
        }
        for(Cookie cookie : req.getCookies()){
            sb.append("cookie:"+cookie.getName()+"-"+cookie.getComment()+"<br>");
        }
        sb.append("method"+req.getMethod()+" URI"+req.getRequestURI());
        Map<String,String[]> map = new HashedMap();
        map = req.getParameterMap();
        for(Map.Entry entry : map.entrySet()){
            sb.append(" paramKey:"+entry.getKey().toString()+" paramVlu:"+entry.getValue().toString()+"<br>");
        }
        return  sb.toString();
    }
    @RequestMapping("/redirect/{param}")
    @ResponseBody
    public RedirectView redirect(@PathVariable(value = "param") int param){
        RedirectView red = new RedirectView("/",true);
        if(param==301){
            red.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        }
        return red;
    }
    @RequestMapping("/admin/{key}")
    @ResponseBody
    public String admin(@PathVariable(value = "key") String key){
        if(key.equals("admin"))
            return "hello admin";
        throw new IllegalArgumentException("KEY ERROR");
    }
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }
}
