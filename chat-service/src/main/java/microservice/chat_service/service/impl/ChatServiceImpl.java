package microservice.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import microservice.chat_service.dto.GroupChatRequest;
import microservice.chat_service.model.Chat;
import microservice.chat_service.model.User;
import microservice.chat_service.repository.ChatRepository;
import microservice.chat_service.service.ChatService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserServiceImpl userService;
    private final ChatRepository chatRepository;

    @Override
    public Chat createChat(User reqUser, String userId) throws AppException {
        User user = userService.findUserById(userId);

        Optional<Chat> isChatExist = chatRepository.findSingleChatByUserIds(userId, reqUser.getId());

        if (isChatExist.isPresent()) {
            return isChatExist.get();
        }

        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);

        return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(String chatId) throws NotFoundException {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("The requested chat is not found"));
    }

    @Override
    public List<Chat> findAllChatByUserId(String userId) throws AppException {
        User user = userService.findUserById(userId);
        List<Chat> chats = chatRepository.findByUsersContaining(user);
        return  chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws AppException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        group.getUsers().add(reqUser);

        for (String userId : req.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }

        return chatRepository.save(group);
    }

    @Override
    public Chat addUserToGroup(String userId, String chatId, User reqUser) throws NotFoundException,AppException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("The expected chat is not found"));

        User user = this.userService.findUserById(userId);

        if (chat.getAdmins().contains((reqUser))) {
            chat.getUsers().add(user);
            return chat;
        } else {
            throw new AppException(ErrorCode.ADMIN_CHAT_EXCEPTION);
        }
    }

    @Override
    public Chat renameGroup(String chatId, String groupName, User reqUser) throws NotFoundException, AppException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("The expected chat is not found"));

        if (chat.getUsers().contains(reqUser)) {
            chat.setChatName(groupName);
            return chatRepository.save(chat);
        } else {
            throw new AppException(ErrorCode.USER_CHAT_EXCEPTION);
        }
    }

    @Override
    public Chat removeFromGroup(String chatId, String userId, User reqUser) throws NotFoundException, AppException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("The expected chat is not found"));

        User user = userService.findUserById(userId);

        if (chat.getAdmins().contains((reqUser))) {
            chat.getUsers().remove(user);
            return chat;
        } else if (chat.getUsers().contains(reqUser)) {
            if (Objects.equals(user.getId(), reqUser.getId())) {
                chat.getUsers().remove(user);
                return this.chatRepository.save(chat);
            }
        }
        throw new AppException(ErrorCode.ADMIN_CHAT_EXCEPTION);
    }

    @Override
    public void deleteChat(String chatId, String userId) throws AppException,NotFoundException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("The expected chat is not found while deleteing"));

        User reqUser = userService.findUserById(userId);

        if (chat.getAdmins().contains((reqUser))) {
            chatRepository.delete(chat);
        } else {
            throw new AppException(ErrorCode.ADMIN_CHAT_EXCEPTION);
        }
    }
}
