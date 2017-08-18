package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.NewsDAO;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sherl on 2017/7/16.
 */
@Service
public class NewsService {
    @Autowired
    NewsDAO newsDAO;

    public List<News> findNewsList(int userId,int offset,int limit){
        return newsDAO.findNewsList(userId,offset,limit);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if(dotpos<0)
            return null;
        String ext = file.getOriginalFilename().substring(dotpos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(ext)){
            return  null;
        }
        String Filename = UUID.randomUUID().toString().replaceAll("-","")+"."+ext;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+Filename).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+Filename;
    }

    public  News getNews(int id){
        return newsDAO.getById(id);
    }
    public int updateCommentCount(int id,int commentCount){
        return newsDAO.updateCommentCount(id,commentCount);
    }

    public int updatelikeCount(int id,int likeCount){
        return newsDAO.updatelikeCount(id,likeCount);
    }
}
