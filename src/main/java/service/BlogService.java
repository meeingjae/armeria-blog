package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.ConsumesJson;
import com.linecorp.armeria.server.annotation.Default;
import com.linecorp.armeria.server.annotation.Description;
import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Order;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesJson;
import com.linecorp.armeria.server.annotation.Put;
import com.linecorp.armeria.server.annotation.RequestConverter;
import com.linecorp.armeria.server.annotation.RequestObject;
import com.linecorp.armeria.server.annotation.ResponseConverter;

import annotations.BlogConsumableType;
import annotations.BlogProducibleType;
import dto.BlogPost;
import dto.BlogPostConverter;
import dto.BlogPostResponseConverter;
import handler.BlogExceptionHandler;

@Description("Doc Description???")
public class BlogService {
    private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

    @ExceptionHandler(BlogExceptionHandler.class) // priority = 1: method, 2: class, 3: annotatedService impl
    @Post("/blogs")
    @BlogConsumableType
    @BlogProducibleType
    @RequestConverter(BlogPostConverter.class)
    @ResponseConverter(BlogPostResponseConverter.class)
    @Description("create blog post api")
    public BlogPost createBlogPost(BlogPost blogPost) { //RequestConverter
        blogPosts.put(blogPost.getId(), blogPost);
        if (blogPost.getTitle() == null) { // exception handler example
            throw new IllegalArgumentException();
        }
        return blogPost; // ResponseConverter
    }

    @ConsumesJson
    @ProducesJson
    @Description("get blog post list")
    @Get("/blogs")
    public Iterable<BlogPost> getBlogPostList(@Param @Default("true") boolean descending) {

        if (descending) {
            return blogPosts.entrySet()
                            .stream()
                            .sorted(Collections.reverseOrder(Comparator.comparingInt(Entry::getKey)))
                            .map(Entry::getValue)
                            .collect(Collectors.toList());
        }
        return new ArrayList<>(blogPosts.values());
    }

    @Order(1) // 동일 메소드 우선순위 제공
    @ExceptionHandler(BlogExceptionHandler.class)
    @Description("get blog post")
    @Get("/blogs/:id")
    public BlogPost getBlogPost(@Param @Default("1") int id) {
        if (!blogPosts.containsKey(id)) {
            throw new NoSuchElementException();
        }
        return blogPosts.get(id);
    }

    @BlogConsumableType
    @BlogProducibleType
    @ExceptionHandler(BlogExceptionHandler.class)
    @Put("/blogs/:id")
    public HttpResponse updateBlogPost(@Param int id, @RequestObject BlogPost blogPost) {
        BlogPost oldPost = blogPosts.get(id);
        if (oldPost == null) {
            throw new NoSuchElementException();
        }
        BlogPost newPost = new BlogPost(id, blogPost.getTitle(), blogPost.getContent()
                , oldPost.getCreatedAt(), System.currentTimeMillis());
        blogPosts.put(id, newPost);
        return HttpResponse.ofJson(newPost);
    }
}
