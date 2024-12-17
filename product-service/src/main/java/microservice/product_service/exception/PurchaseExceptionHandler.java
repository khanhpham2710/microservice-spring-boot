package microservice.product_service.exception;


import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class PurchaseExceptionHandler {
    @ExceptionHandler(value = PurchaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePurchaseException(PurchaseException exception, WebRequest request) {

        return ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(new Date())
                .errorCode(ErrorCode.PURCHASE_EXCEPTION.getCode())
                .message(exception.getMessage())
                .build();
    }
}
