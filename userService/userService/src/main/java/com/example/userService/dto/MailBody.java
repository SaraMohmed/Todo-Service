package com.example.userService.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MailBody{
    private String to;
    private String subject;
    private String text;

}
