package handler;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ExceptionHandlerFunction;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;
import com.linecorp.armeria.server.annotation.ResponseConverterFunction;

import dto.BlogPost;

public class BlogPostAllInOneHandler implements RequestConverterFunction,
                                                ResponseConverterFunction,
                                                ExceptionHandlerFunction {
    private static final ObjectMapper mapper = new ObjectMapper();
    private AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public HttpResponse handleException(ServiceRequestContext ctx, HttpRequest req, Throwable cause) {
        if (isIllegalArgumentException(cause)) { // IllegalArgumentException return 400(default)
            return HttpResponse.of(HttpStatus.BAD_REQUEST); // but declared for testing
        }
        return ExceptionHandlerFunction.fallthrough();
    }

    @Override
    public @Nullable Object convertRequest(ServiceRequestContext ctx, AggregatedHttpRequest request,
                                           Class<?> expectedResultType,
                                           @Nullable ParameterizedType expectedParameterizedResultType)
            throws Exception {
        if (expectedResultType == BlogPost.class) {
            JsonNode jsonNode = mapper.readTree(request.contentUtf8());
            int id = idGenerator.getAndIncrement();
            String title = stringValue(jsonNode, "title");
            String content = stringValue(jsonNode, "content");
            return new BlogPost(id, title, content);
        }
        return RequestConverterFunction.fallthrough();
    }

    @Override
    public HttpResponse convertResponse(ServiceRequestContext ctx, ResponseHeaders headers,
                                        @Nullable Object result, HttpHeaders trailers) throws Exception {
        if (result instanceof BlogPost) {
            return HttpResponse.of(HttpStatus.OK,
                                   MediaType.JSON_UTF_8,
                                   "%S", (mapper.writeValueAsString((BlogPost) result)),
                                   trailers);
        }
        return ResponseConverterFunction.fallthrough();
    }

    static String stringValue(JsonNode jsonNode, String field) {
        JsonNode value = jsonNode.get(field);
        if (value == null) {
            throw new IllegalArgumentException(field + " is missing!!");
        }
        return value.textValue();
    }

    private boolean isIllegalArgumentException(Throwable cause) {
        return cause instanceof IllegalArgumentException;
    }
}
