package microservice.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.chat_service.dto.SendMessageRequest;
import microservice.chat_service.model.Chat;
import microservice.chat_service.model.Message;
import microservice.chat_service.model.User;
import microservice.chat_service.repository.MessageRepository;
import microservice.chat_service.service.MessageService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.NotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserServiceImpl userService;
    private final ChatServiceImpl chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message sendMessage(SendMessageRequest req) throws AppException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = Message.builder()
                .chat(chat)
                .user(user)
                .content(req.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        message = messageRepository.save(message);

        // Send message to WebSocket topic based on chat type
        if (chat.isGroup()) {
            messagingTemplate.convertAndSend("/group/" + chat.getId(), message);
        } else {
            messagingTemplate.convertAndSend("/user/" + chat.getId() + "/private", message);
        }

        return message;
    }

    @Override
    public List<Message> getChatsMessages(String chatId, User reqUser) throws AppException {
        Chat chat = chatService.findChatById(chatId);

        boolean userInChat = chat.getUsers().stream()
                .anyMatch(user -> user.getId().equals(reqUser.getId()));

        if (!userInChat) {
            throw new AppException(ErrorCode.USER_CHAT_EXCEPTION);
        }

        return messageRepository.findByChatId(chat.getId());
    }

    @Override
    public Message findMessageById(String messageId) throws NotFoundException {
        return this.messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("The required message is not found"));
    }

    @Override
    public void deleteMessage(String messageId, User reqUser) throws AppException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("The required message is not found"));

        if (Objects.equals(message.getUser().getId(), reqUser.getId())) {
            messageRepository.delete(message);
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
