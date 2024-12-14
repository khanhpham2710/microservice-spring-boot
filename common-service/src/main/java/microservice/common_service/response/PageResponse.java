package microservice.common_service.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private String message;
    private PageData<T> pageData;
}
