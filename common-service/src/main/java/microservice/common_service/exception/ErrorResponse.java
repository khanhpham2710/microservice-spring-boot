package microservice.common_service.exception;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Date timestamp;
    private int errorCode;
    private String path;
    private String message;
}
