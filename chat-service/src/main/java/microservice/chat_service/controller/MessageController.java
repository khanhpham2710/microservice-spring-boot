package microservice.chat_service.controller;

import lombok.RequiredArgsConstructor;
import microservice.chat_service.dto.SendMessageRequest;
import microservice.chat_service.model.Message;
import microservice.chat_service.model.User;
import microservice.chat_service.service.MessageService;
import microservice.chat_service.service.UserService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest sendMessageRequest,
                                                      @RequestHeader("Authorization") String jwt) throws NotFoundException, AppException {
        User user = userService.findUserProfile(jwt);
        sendMessageRequest.setUserId(user.getId());
        Message message = messageService.sendMessage(sendMessageRequest);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<Message>> getChatMessageHandler(@PathVariable String chatId,
                                                               @RequestHeader("Authorization") String jwt) throws NotFoundException, AppException {
        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessages(chatId, user);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessageHandler(@PathVariable String messageId,
                                                            @RequestHeader("Authorization") String jwt) throws NotFoundException, AppException {
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);

        return new ResponseEntity<>("Deleted successfully......", HttpStatus.OK);
    }
}
