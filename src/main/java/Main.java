import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;

import service.BlogService;

public final class Main {

    private static final int PORT = 9090;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

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

        ServerBuilder serverBuilder = Server.builder();
        DocService docService =
                DocService.builder()
                          .exampleRequests(BlogService.class,
                                           "createBlogPost",
                                           "{\"title\":\"My first blog\", \"content\":\"Hello Mingble!\"}")
                          .build();
        return serverBuilder.http(port)
                            .service("/", ((ctx, req) -> HttpResponse.of("Hi Mingble!")))
                            .annotatedService(new BlogService())
                            .serviceUnder("/docs", docService)
                            .build();
    }
}
