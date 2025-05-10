package com.example.publicdatanotification.firebase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class NotificationDto {
    private String to;
    private Notification notification;
    public NotificationDto(String to){
        this.to = to;
    }
    @Getter@Setter@Builder
    public static class Notification{
        private String title;
        private String body;
        private String mutable_content;
    }
}
