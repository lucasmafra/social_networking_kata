package com.lucasmafra.socialnetworking.acceptance;

import com.lucasmafra.socialnetworking.domain.entities.Post;
import com.lucasmafra.socialnetworking.domain.gateways.PostGateway;
import com.lucasmafra.socialnetworking.presentation.console.ConsoleSocialNetworking;
import com.lucasmafra.socialnetworking.stubs.AppContextStub;
import com.lucasmafra.socialnetworking.stubs.ClockStub;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PostingTest {

    private AppContextStub context;

    private ConsoleSocialNetworking app;

    private static final Date NOW = ClockStub.asDate(2018, Calendar.APRIL, 6, 8, 4, 0);

    @Before
    public void
    initialize() {
        context = AppContextStub.getInstance();
        app = new ConsoleSocialNetworking(context);
    }

    @Test
    public void
    user_can_publish_message_to_personal_timeline() {

        // Given
        context.getBufferedReader().setNextLine("Alice -> I love the weather today");
        context.getClock().setNow(NOW);

        // When
        app.processInput();

        // Then
        PostGateway postGateway = context.getStorageContext().getPostGateway();
        List<Post> userPosts = postGateway.getPostsInReverseChronologicalOrder("Alice");
        assertThat("expect that 1 post was created", userPosts.size(), is(1));
        Post post = userPosts.get(0);
        assertThat("expect that the created post has correct user id", post.getUserId(), is("Alice"));
        assertThat("expect that the created post has correct message", post.getMessage(), is("I love the weather today"));
        assertThat("expect that the created post has correct created date", post.getCreatedDate(), is(NOW));
    }
}