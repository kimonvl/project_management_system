package com.kimon.project_management_system.service;

import com.kimon.project_management_system.model.Chat;
import com.kimon.project_management_system.repo.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatRepo chatRepo;

    @Override
    public Chat createChat(Chat chat) throws Exception {
        return chatRepo.save(chat);
    }
}
