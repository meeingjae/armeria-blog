import static docs.ServerDocs.makeBlogDocService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;

import dto.BlogPostConverter;
import dto.BlogPostResponseConverter;
import handler.BlogExceptionHandler;
import handler.BlogPostAllInOneHandler;
import service.BlogService;

public final class Main {

    private static final int PORT = 9090;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Server server = newServer(PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Server has been stopped");
        }));

        server.start().join();

        logger.info("Server had been started. Serving dummy service at http://127.0.0.1:{}",
                    server.activeLocalPort());
    }

    static Server newServer(int port) {

        return Server.builder().http(port)
                     .service("/", ((ctx, req) -> HttpResponse.of("Hi Mingble!")))
                     .annotatedService(new BlogService(),
                                       new BlogExceptionHandler(),
                                       new BlogPostConverter(),
                                       new BlogPostResponseConverter(),
                                       new BlogPostAllInOneHandler()) // Converter + Handler
                     .serviceUnder("/docs", makeBlogDocService())
                     .build();
    }
}
