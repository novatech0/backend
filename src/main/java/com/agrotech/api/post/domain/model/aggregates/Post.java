package com.agrotech.api.post.domain.model.aggregates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Long advisorId;

}
