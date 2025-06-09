package com.agrotech.api.post.domain.model.aggregates;

import com.agrotech.api.shared.infrastructure.persistence.jpa.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AuditableEntity {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Long advisorId;

}
