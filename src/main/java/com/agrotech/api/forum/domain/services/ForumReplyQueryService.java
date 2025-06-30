package com.agrotech.api.forum.domain.services;


import com.agrotech.api.forum.domain.model.entities.ForumReply;
import com.agrotech.api.forum.domain.model.queries.GetAllForumRepliesFromForumPostIdQuery;

import java.util.List;

public interface ForumReplyQueryService {
    List<ForumReply> handle(GetAllForumRepliesFromForumPostIdQuery query);
}
