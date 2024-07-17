package com.example.userService.services;

import com.example.userService.dto.MailBody;

public interface EmailService {

    void sendSimpleMessage(MailBody mailBody);
}
