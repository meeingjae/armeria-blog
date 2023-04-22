package armeria.handler;

import java.util.NoSuchElementException;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ExceptionHandlerFunction;

import io.micrometer.common.lang.NonNull;

public class BlogExceptionHandler implements ExceptionHandlerFunction {

    @NonNull
    @Override
    public HttpResponse handleException(@NonNull ServiceRequestContext ctx,
                                        @NonNull HttpRequest req,
                                        @NonNull Throwable cause) {
        if (isIllegalArgumentException(cause)) { // IllegalArgumentException return 400(default)
            return HttpResponse.of(HttpStatus.BAD_REQUEST); // but declared for testing
        } else if (isNotFoundException(cause)) {
            return HttpResponse.of(HttpStatus.NOT_FOUND);
        }
        return ExceptionHandlerFunction.fallthrough();
    }

    private boolean isIllegalArgumentException(Throwable cause) {
        return cause instanceof IllegalArgumentException;
    }

    private boolean isNotFoundException(Throwable cause) {
        return cause instanceof NoSuchElementException;
    }
}
