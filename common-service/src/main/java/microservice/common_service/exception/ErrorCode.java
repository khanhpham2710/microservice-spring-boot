package microservice.common_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAM(1000,"Multiple invalid request",HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD(1002, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1003, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    USER_EXISTED(1010, "Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1011, "User not existed", HttpStatus.NOT_FOUND),
    WRONG_AUTHENTICATION(1012, "Wrong email or password", HttpStatus.BAD_REQUEST),

    EMAIL_SEND_FAILED(1020,"Send email failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1030, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1040, "You do not have permission", HttpStatus.FORBIDDEN),

    PURCHASE_EXCEPTION(1050, "Purchase exception", HttpStatus.BAD_REQUEST);


    private int code;
    private String message;
    private HttpStatus statusCode;
}
