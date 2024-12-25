package microservice.chat_service.service;

import microservice.chat_service.dto.SendMessageRequest;
import microservice.chat_service.model.Message;
import microservice.chat_service.model.User;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.NotFoundException;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest req) throws AppException;

    public List<Message> getChatsMessages(String chatId, User reqUser) throws AppException;

    public Message findMessageById(String messageId) throws NotFoundException;

    public void deleteMessage(String messageId, User reqUser) throws AppException;

}
