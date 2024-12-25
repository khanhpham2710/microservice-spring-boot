package microservice.chat_service.service;

import microservice.chat_service.dto.GroupChatRequest;
import microservice.chat_service.model.Chat;
import microservice.chat_service.model.User;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.NotFoundException;

import java.util.List;

public interface ChatService {
    public Chat createChat(User reqUser, String userId) throws AppException;

    public Chat findChatById(String chatId) throws NotFoundException;

    public List<Chat> findAllChatByUserId(String userId) throws AppException;

    public Chat createGroup(GroupChatRequest req, User reqUser) throws AppException;

    public Chat addUserToGroup(String userId, String chatId, User reqUser) throws NotFoundException, AppException;

    public Chat renameGroup(String chatId, String groupName, User reqUser) throws NotFoundException, AppException;

    public Chat removeFromGroup(String chatId, String userId, User reqUser) throws NotFoundException, AppException;

    public void deleteChat(String chatId, String userId) throws AppException;
}
