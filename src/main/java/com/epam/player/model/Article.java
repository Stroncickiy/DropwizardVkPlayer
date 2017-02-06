package com.epam.player.model;

import java.util.Date;

/**
 * Created by Yaroslav_Strontsitsk on 1/29/2016.
 */
public class Article {

    private String title;
    private String content;
    private User author;
    private Date created;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
