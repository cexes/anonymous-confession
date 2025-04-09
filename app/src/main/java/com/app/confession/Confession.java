package com.app.confession;

public class Confession {
    private  Integer id;
    private String content;
    private Integer like;

    public Integer getLke() { return  like; }
    public  String getContent(){ return  content; }
    public  Integer getLike(){ return like; }

    public  void setId(Integer id) { this.id = id; };
    public  void setContent(String content) { this.content = content; };
    public  void setLike(Integer like) { this.like = like; };

}
