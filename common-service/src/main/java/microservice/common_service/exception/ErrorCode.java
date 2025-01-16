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


    NOT_VERIFY_EMAIL(1001, "Please verify email", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD(1002, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1003, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_EQUAL(1004, "Password and confirm password must be the same", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN(1005, "Invalid refresh token", HttpStatus.BAD_REQUEST),
    INACTIVE_REFRESH_TOKEN(1006, "Inactive refresh token", HttpStatus.BAD_REQUEST),

    USER_EXISTED(1010, "Email existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1011, "User not existed", HttpStatus.NOT_FOUND),
    WRONG_AUTHENTICATION(1012, "Wrong password", HttpStatus.BAD_REQUEST),

    EMAIL_SEND_FAILED(1020,"Send email failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1030, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1040, "You do not have permission", HttpStatus.FORBIDDEN),

    PURCHASE_EXCEPTION(1050, "Purchase exception", HttpStatus.BAD_REQUEST),
    ADMIN_CHAT_EXCEPTION(1060, "This is an chat admin function", HttpStatus.BAD_REQUEST),
    USER_CHAT_EXCEPTION(1061, "You are not a part of this chat", HttpStatus.BAD_REQUEST),
    MAX_SIZE_IMAGE(1070,"Max file size is 2MB",HttpStatus.BAD_REQUEST),
    UNALLOWED_FILE_EXTENSION(1071,"Only jpg, png, gif, bmp files are allowed",HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAIL(1072,"Failed to upload file",HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_IMAGE_FAIL(1073,"Failed to delete file",HttpStatus.INTERNAL_SERVER_ERROR);;


    private int code;
    private String message;
    private HttpStatus statusCode;
}
