package com.agrotech.api.post.application.internal.commandservices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.agrotech.api.post.application.internal.outboundservices.acl.ExternalProfileService;
import com.agrotech.api.post.domain.model.aggregates.Post;
import com.agrotech.api.post.domain.model.commands.CreatePostCommand;
import com.agrotech.api.post.infrastructure.persistence.jpa.repositories.PostRepository;
import com.agrotech.api.profile.domain.model.entities.Advisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class PostCommandServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private ExternalProfileService externalProfileService;
    @InjectMocks
    private PostCommandServiceImpl postCommandService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // Creating a post with valid advisor ID returns the post ID
    @Test
    public void test_handle_create_post_command_with_valid_advisor_returns_post_id() {
        // Arrange
        Long advisorId = 1L;
        Long expectedPostId = 10L;
        String title = "Test Title";
        String description = "Test Description";
        String image = "test-image.jpg";

        Advisor advisor = mock(Advisor.class);
        when(advisor.getId()).thenReturn(advisorId);
        CreatePostCommand command = new CreatePostCommand(advisor.getId(), title, description, image);



        Post post = mock(Post.class);
        when(post.getId()).thenReturn(expectedPostId);
        when(post.getAdvisorId()).thenReturn(advisorId);

        when(externalProfileService.fetchAdvisorById(advisorId)).thenReturn(Optional.of(advisor));


        // Mock the save method to set ID and return the post

        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        Long actualPostId = postCommandService.handle(command);

        // Assert
        assertEquals(expectedPostId, actualPostId);
        verify(externalProfileService).fetchAdvisorById(advisor.getId());
        verify(postRepository).save(any(Post.class));
    }
}