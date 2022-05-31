package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Description;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.RequestConverter;

import dto.BlogPost;
import dto.BlogPostConverter;

@Description("Doc Description???")
public class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

    @Post("/blogs")
    @RequestConverter(BlogPostConverter.class)
    public HttpResponse createBlogPost(BlogPost blogPost) {
        blogPosts.put(blogPost.getId(), blogPost);
        return HttpResponse.ofJson(blogPost);
    }
}
