package microservice.common_service.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "min";

    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        String message = exception.getMessage();

        if (exception instanceof MissingServletRequestParameterException) {
            errorResponse.setErrorCode(ErrorCode.INVALID_PARAM.getCode());
            errorResponse.setMessage(message);
        } else if (exception instanceof ConstraintViolationException) {
            errorResponse.setErrorCode(ErrorCode.INVALID_PARAM.getCode());
            String reason = message.substring(message.indexOf(" ") + 1);
            String param = message.substring(message.indexOf(".")+1,message.indexOf(":"));
            errorResponse.setMessage(param + reason);
        } else {
            errorResponse.setErrorCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
            errorResponse.setMessage(message);
        }
        return errorResponse;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultipleErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                    WebRequest request) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();

            if (message != null && message.contains("{") && message.contains("}")) {
                message = mapAttribute(message, (Map<String, Object>) error.unwrap(ConstraintViolation.class));
            }

            errors.add(message);
        });


        return MultipleErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(new Date())
                .errorCode(ErrorCode.INVALID_PARAM.getCode())
                .messages(errors)
                .build();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception,WebRequest request) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ErrorResponse.builder()
                        .path(request.getDescription(false).replace("uri=", ""))
                        .timestamp(new Date())
                        .errorCode(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build();
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception,WebRequest request) {

        return ErrorResponse.builder()
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(new Date())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handlingAppException(AppException exception, WebRequest request){
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse apiResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .message(errorCode.getMessage())
                .errorCode(errorCode.getCode())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleException(Exception e, WebRequest request) {
//        return ErrorResponse.builder()
//                .timestamp(new Date())
//                .path(request.getDescription(false).replace("uri=", ""))
//                .errorCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                .build();
//    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }
}
