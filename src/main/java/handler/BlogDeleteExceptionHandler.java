package handler;

import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ExceptionHandlerFunction;

public class BlogDeleteExceptionHandler implements ExceptionHandlerFunction {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public HttpResponse handleException(ServiceRequestContext ctx, HttpRequest req, Throwable cause) {
        if (isNoSuchElementException(cause)) {
            String message = cause.getMessage();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("error", message);
            return HttpResponse.ofJson(HttpStatus.NOT_FOUND, objectNode);
        }
        return ExceptionHandlerFunction.fallthrough();
    }

    private boolean isNoSuchElementException(Throwable cause) {
        return cause instanceof NoSuchElementException;
    }
}
