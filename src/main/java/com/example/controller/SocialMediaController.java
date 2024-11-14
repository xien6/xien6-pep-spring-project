package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity register(@RequestBody Account user){
        if (user.getUsername().length() > 0 && user.getPassword().length() >= 4){
            if(Does_User_Exists_Helper(user) == null){
                Account ret = accountService.addUser(user);
                return ResponseEntity.status(200).body(ret);
            }else{
                return ResponseEntity.status(409).body("Conflict Error");
            }
        }else{
            return ResponseEntity.status(400).body("Client Error");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody Account user){
        Account check = Does_User_Exists_Helper(user);
        if (check != null){
            if (check.getUsername().equals(user.getUsername()) && check.getPassword().equals(user.getPassword())){
                return ResponseEntity.status(200).body(check);
            }
        }
        return ResponseEntity.status(401).body("Unauthorized Error");
    }

    @PostMapping("/messages")
    @ResponseBody
    public ResponseEntity create_New_Message(@RequestBody Message message){
        if(message.getMessageText().length() > 0 && message.getMessageText().length() < 255 && Does_UserID_Exists_Helper(message)){
            Message ret = messageService.addMessage(message);
            return ResponseEntity.status(200).body(ret);
        }else{
            return ResponseEntity.status(400).body("Client Error");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity get_All_Messages(){
        List<Message> ret = messageService.getAllMessages();
        return ResponseEntity.status(200).body(ret);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity get_Message_ByID(@PathVariable Integer message_id){
        Message ret = messageService.getMessagebyID(message_id);
        return ResponseEntity.status(200).body(ret);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity delete_Message_ByID(@PathVariable Integer message_id){
        if(messageService.getMessagebyID(message_id) != null){
            messageService.deleteMessagebyID(message_id);
            return ResponseEntity.status(200).body(1);
        }else{
            return ResponseEntity.status(200).body(null);
        }
    }

    @PatchMapping("/messages/{message_id}")
    @ResponseBody
    public ResponseEntity update_Message_ByID(@PathVariable Integer message_id, @RequestBody Message message){
        if(messageService.getMessagebyID(message_id) != null){
            if(message.getMessageText().length() > 0 && message.getMessageText().length() < 255){
                messageService.updateMessage(message_id, message);
                return ResponseEntity.status(200).body(1);
            }
        }
        return ResponseEntity.status(400).body("Client Error");
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity get_All_Messages_ByID(@PathVariable Integer account_id){
        List<Message> ret = messageService.getAllMessagesbyID(account_id);
        return ResponseEntity.status(200).body(ret);
    }

//Helpers ------------------------------------------------------------------

    private Account Does_User_Exists_Helper(Account user){
        List<Account> check = accountService.getAllUsers();
        for (Account element : check){
            if (element.getUsername().equals(user.getUsername())){
                return element;
            }
        }
        return null;
    }

    private Boolean Does_UserID_Exists_Helper(Message message){
        List<Account> check = accountService.getAllUsers();
        for (Account element : check){
            if (element.getAccountId().equals(message.getPostedBy())){
                return true;
            }
        }
        return false;
    }
}
