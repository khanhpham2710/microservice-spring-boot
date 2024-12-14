package microservice.common_service.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageData<T> {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private List<T> items;
}
