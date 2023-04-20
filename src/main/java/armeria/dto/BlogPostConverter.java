package armeria.dto;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;

public class BlogPostConverter implements RequestConverterFunction {
    private static final ObjectMapper mapper = new ObjectMapper();
    private AtomicInteger idGenerator = new AtomicInteger(); //Blog Id Generator

    @Override
    public @Nullable Object convertRequest(ServiceRequestContext ctx, AggregatedHttpRequest request,
                                           Class<?> expectedResultType,
                                           @Nullable ParameterizedType expectedParameterizedResultType)
            throws Exception {
        if (isBlogPostType(expectedResultType)) {
            JsonNode jsonNode = mapper.readTree(request.contentUtf8());
            int id = idGenerator.getAndIncrement();
            String title = stringValue(jsonNode, "title");
            String content = stringValue(jsonNode, "content");
            return new BlogPost(id, title, content);
        }
        return RequestConverterFunction.fallthrough();
    }

    static String stringValue(JsonNode jsonNode, String field) {
        JsonNode value = jsonNode.get(field);
        if (value == null) {
            throw new IllegalArgumentException(field + " is missing!!");
        }
        return value.textValue();
    }

    private boolean isBlogPostType(Class<?> exceptedResultType) {
        return exceptedResultType == BlogPost.class;
    }
}
