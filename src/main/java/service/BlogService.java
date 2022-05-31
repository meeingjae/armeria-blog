package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Description;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.RequestConverter;
import com.linecorp.armeria.server.annotation.ResponseConverter;

import dto.BlogPost;
import dto.BlogPostConverter;
import dto.BlogPostResponseConverter;
import handler.BlogExceptionHandler;

@Description("Doc Description???")
public class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

    @ExceptionHandler(BlogExceptionHandler.class)
    @Post("/blogs")
    @RequestConverter(BlogPostConverter.class)
    @ResponseConverter(BlogPostResponseConverter.class)
    public BlogPost createBlogPost(BlogPost blogPost) { //RequestConverter
        blogPosts.put(blogPost.getId(), blogPost);
        if(blogPost.getTitle() == null){ // exception handler example
            throw new IllegalArgumentException();
        }
        return blogPost; // ResponseConverter
    }
}
