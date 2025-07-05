package com.agrotech.api.forum.domain.model.aggregates;

import com.agrotech.api.forum.domain.model.commands.CreateForumPostCommand;
import com.agrotech.api.iam.domain.model.aggregates.User;
import lombok.Getter;

@Getter
public class ForumPost {
    private Long id;
    private User user;
    private String title;
    private String content;

    public ForumPost() {}

    public ForumPost(Long id, String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public ForumPost(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static ForumPost create(CreateForumPostCommand command, User user) {
        return new ForumPost(command.title(), command.content(), user);
    }

    public Long getUserId() {
        return this.user.getId();
    }
}