package com.herawi.sigma.services;

import com.herawi.sigma.models.Account;
import com.herawi.sigma.models.Notification;
import com.herawi.sigma.repositories.NotificationRepository;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountService accountService;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, AccountService accountService) {
        this.notificationRepository = notificationRepository;
        this.accountService = accountService;
    }

    public Collection<Notification> getAllNotifications(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Collection<Notification> notifications = new ArrayList<>();
        Account account = null;
        if(email != null){
            account = accountService.getAccountWithDetails(email);
            System.out.println("user id: "+account.getId());
        }
        if(account != null){
            notifications = notificationRepository.findAllByUserId(account.getId());
        }
        return notifications;
    }

    public Collection<Notification> getAllUnSeenNotification(HttpServletRequest request){
        String email = JWTTools.getUserEmailByJWT(request);
        Collection<Notification> notifications = new ArrayList<>();
        Account account = null;
        if(email != null){
            account = accountService.getAccountWithDetails(email);
        }
        if(account != null){
            notifications = notificationRepository.findAllBySeenFalseAndUserId(account.getId());
        }
        return notifications;
    }
    /*
    * add new notification in the database
    * */
    public Notification addNotification(Notification notification){
        return notificationRepository.save(notification);
    }
}
