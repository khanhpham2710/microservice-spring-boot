package microservice.chat_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;

    @Field("chat_name")
    private String chatName;

    @Field("chat_image")
    private String chatImage;

    @Field("is_group")
    private boolean isGroup;

    @DBRef
    private Set<User> admins = new HashSet<>();

    @DBRef
    private User createdBy;

    @DBRef
    private Set<User> users = new HashSet<>();

    @DBRef
    private List<Message> messages;
}
