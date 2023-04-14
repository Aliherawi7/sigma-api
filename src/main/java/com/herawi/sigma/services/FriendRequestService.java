package com.herawi.sigma.services;

import com.herawi.sigma.dto.AccountDTO;
import com.herawi.sigma.dto.FriendRequestRegisterationDTO;
import com.herawi.sigma.models.FriendRequest;
import com.herawi.sigma.models.Notification;
import com.herawi.sigma.repositories.FriendRequestRepository;
import com.herawi.sigma.utils.JWTTools;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final NotificationService notificationService;
    private final AccountService accountService;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, NotificationService notificationService, AccountService accountService) {
        this.friendRequestRepository = friendRequestRepository;
        this.notificationService = notificationService;
        this.accountService = accountService;
    }

    /*
    * get all friend requests by senderId
    * */
    public Collection<FriendRequest> getAllFriendRequestsBySenderId(String senderUserName){
        return friendRequestRepository.findAllByRequestSenderUserName(senderUserName);
    }
    /*
    * get all friend requests by receiverId
    * */
    public Collection<AccountDTO> getAllFriendRequestsByReceiverId(String receiverUserName){
        return friendRequestRepository.findAllByRequestReceiverUserName(receiverUserName)
                .stream().map( friendRequest -> accountService.getAccountByUserName(friendRequest.getRequestSenderUserName()))
                .collect(Collectors.toList());
    }
    /*
    * add a new friend request to the database for the receiver account and create a notification
    * for the receiver account
    * */
    public FriendRequest addFriendRequest(HttpServletRequest request, FriendRequestRegisterationDTO friendRequestRegisterationDTO){
        String email = JWTTools.getUserEmailByJWT(request);
        String username = accountService.getAccount(email).getUserName();
        if(!username.equalsIgnoreCase(friendRequestRegisterationDTO.getRequestSenderUserName()))
            throw new InvalidParameterException("Invalid sender username");
        if(friendRequestRegisterationDTO.getRequestReceiverUserName()
                .equalsIgnoreCase(friendRequestRegisterationDTO.getRequestSenderUserName())){
            throw new InvalidParameterException("Invalid receiver username");
        }

        if(friendRequestRepository.
                findByRequestReceiverUserNameAndRequestSenderUserName(
                        friendRequestRegisterationDTO.getRequestReceiverUserName(),
                        friendRequestRegisterationDTO.getRequestSenderUserName()) != null){
            throw new InvalidParameterException("You have already sent friend request");
        }
        if(accountService.isFriend(friendRequestRegisterationDTO.getRequestSenderUserName(), friendRequestRegisterationDTO.getRequestReceiverUserName())){
            throw new InvalidParameterException("You are already friends together");
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequestSenderUserName(friendRequestRegisterationDTO.getRequestSenderUserName());
        friendRequest.setRequestReceiverUserName(friendRequestRegisterationDTO.getRequestReceiverUserName());
        Notification notification = new Notification();
        notification.setTitle("Friend request");
        notification.setMessage("You have a new friend request from @" + friendRequest.getRequestSenderUserName());
        notification.setUserName(friendRequest.getRequestReceiverUserName());
        notificationService.addNotification(notification);
        return friendRequestRepository.save(friendRequest);
    }

    /*
    * accept friend request and then remove the friend-request entity from database
    * */
    public void acceptFriendRequest(HttpServletRequest request, FriendRequestRegisterationDTO friendRequestRegisterationDTO){
        String userName = accountService.getAccountWithDetails(JWTTools.getUserEmailByJWT(request)).getUserName();
        boolean isFriend = accountService.getAllFriends(userName)
                .stream()
                .anyMatch(friend -> friend.getUserName().equalsIgnoreCase(friendRequestRegisterationDTO.getRequestSenderUserName()));
        if(!isFriend){
            accountService.addAsFriend(request, friendRequestRegisterationDTO.getRequestSenderUserName());

            removeFriendRequest(friendRequestRegisterationDTO);
        }
    }

    public boolean deleteFriendRequest(long id){
        if(friendRequestRepository.findById(id).isPresent()){
            friendRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }
    /*
    * an account can delete or reject only the friend requests that are belong to its
    * */
    public void rejectFriendRequest(String receiverUserName, String senderUserName){
        if(receiverUserName == null || senderUserName == null) return;
        FriendRequest friendRequest = friendRequestRepository
                .findByRequestReceiverUserNameAndRequestSenderUserName(receiverUserName, senderUserName);
        if(friendRequest != null){
            friendRequestRepository.deleteById(friendRequest.getId());
        }
    }

    public void removeFriendRequest(FriendRequestRegisterationDTO friendRequestRegisterationDTO){
        friendRequestRepository.deleteById(
                friendRequestRepository.findByRequestReceiverUserNameAndRequestSenderUserName(
                        friendRequestRegisterationDTO.getRequestReceiverUserName(),
                        friendRequestRegisterationDTO.getRequestSenderUserName()
                ).getId()
        );
    }

}
