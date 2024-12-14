package microservice.common_service.exception;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleErrorResponse {
    private Date timestamp;
    private int errorCode;
    private String path;
    private Set<String> messages;
}
