package com.herawi.sigma.controllers;

import com.herawi.sigma.models.Notification;
import com.herawi.sigma.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping(value = "api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @GetMapping
    public ResponseEntity<Collection<Notification>> getAllNotification(HttpServletRequest request){
        System.out.println("in get all ");
        Collection<Notification> notifications = notificationService.getAllNotifications(request);

        if(notifications.size() > 0){
            return ResponseEntity.ok(notifications);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest")
    public ResponseEntity<Collection<Notification>> getAllUnSeenNotification(HttpServletRequest request){
        Collection<Notification> notifications = notificationService.getAllUnSeenNotification(request);
        if(notifications.size() > 0){
            return ResponseEntity.ok(notifications);
        }
        return ResponseEntity.noContent().build();
    }
}
