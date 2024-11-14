package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesbyID(Integer userId){
        List<Message> ret = new ArrayList<Message>();
        for (Message element : messageRepository.findAll()){
            if (element.getPostedBy().equals(userId)){
                ret.add(element);
            }
        }
        return ret;
    }

    public Message getMessagebyID(Integer messageId){
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if(messageOptional.isPresent()){
            Message message = messageOptional.get();
            return message;
        }
        return null;
    }

    public void deleteMessagebyID(Integer messageId){
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Integer id, Message replacement){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            message.setMessageText(replacement.getMessageText());
            return messageRepository.save(message);
        }
        return null;
    }
}