package com.agrotech.api.forum.domain.model.entities;

import com.agrotech.api.forum.domain.model.aggregates.ForumPost;
import com.agrotech.api.forum.domain.model.commands.CreateForumReplyCommand;
import com.agrotech.api.iam.domain.model.aggregates.User;
import lombok.Getter;

@Getter
public class ForumReply {
    private Long id;
    private ForumPost forumPost;
    private User user;
    private String content;

    public ForumReply() {}

    public ForumReply(Long id, String content, ForumPost forumPost, User user) {
        this.id = id;
        this.content = content;
        this.forumPost = forumPost;
        this.user = user;
    }

    public ForumReply(String content, ForumPost forumPost, User user) {
        this.content = content;
        this.forumPost = forumPost;
        this.user = user;
    }

    public static ForumReply create(CreateForumReplyCommand command, ForumPost post, User user) {
        return new ForumReply(command.content(), post, user);
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public Long getForumPostId() {
        return this.forumPost.getId();
    }
}
