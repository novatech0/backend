package com.agrotech.api.forum.domain.model.entities;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.domain.model.commands.CreateForumFavoriteCommand;
import com.agrotech.api.iam.domain.model.aggregates.User;
import lombok.Getter;

@Getter
public class ForumFavorite {
    private Long id;
    private User user;
    private ForumPost forumPost;

    public ForumFavorite() {}

    public ForumFavorite(Long id, User user, ForumPost forumPost) {
        this.id = id;
        this.user = user;
        this.forumPost = forumPost;
    }

    public ForumFavorite(User user, ForumPost forumPost) {
        this.user = user;
        this.forumPost = forumPost;
    }

    public static ForumFavorite create(User user, ForumPost forumPost) {
        return new ForumFavorite(user, forumPost);
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public Long getForumPostId() {
        return this.forumPost.getId();
    }
}
