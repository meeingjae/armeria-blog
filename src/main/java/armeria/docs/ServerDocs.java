package armeria.docs;

import com.linecorp.armeria.server.docs.DocService;

import armeria.service.BlogService;

public class ServerDocs {

    private static final String BLOG_ID_URL = "/blogs/:id";

    private static final String CREATE_POST_REQ_EXAMPLE =
            "{\"title\":\"My first blog\", \"content\":\"Hello Mingble!\"}";
    private static final String UPDATE_POST_REQ_EXAMPLE =
            "{\"id\":1,\"title\":\"My first blog123\", \"content\":\"Hello Mingble123!\"}";

    public static DocService makeBlogDocService() {
        return DocService.builder()
                         .exampleRequests(BlogService.class, "createBlogPost", CREATE_POST_REQ_EXAMPLE)
                         .exampleRequests(BlogService.class, "getBlogPostList")
                         .examplePaths(BlogService.class, "getBlogPost", BLOG_ID_URL)
                         .examplePaths(BlogService.class, "updateBlogPost", BLOG_ID_URL)
                         .exampleRequests(BlogService.class, "updateBlogPost", UPDATE_POST_REQ_EXAMPLE)
                         .examplePaths(BlogService.class, "deleteBlogPost", BLOG_ID_URL)
                         .build();
    }
}
