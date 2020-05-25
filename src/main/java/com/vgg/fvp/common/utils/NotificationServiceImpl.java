package com.vgg.fvp.common.utils;

import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository repo;
    private UserService userService;

    public NotificationServiceImpl(NotificationRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Override
    public Optional<Notification> getNotification(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Notification> notifications(User user) {
        return repo.findAllBySubjectUser(user);
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = getNotification(id).orElseThrow(()-> new ObjectNotFoundException("Notification not found"));
        repo.delete(notification);
    }


    @Override
    public Page<Notification> getNotificationByUserAndStatus(Long userId, Optional<String> status, Pageable pageable) {
        User user = userService.getUser(userId).orElseThrow(()-> new ObjectNotFoundException("User not found"));
        if(status.isPresent()){
            return repo.findAllBySubjectUserAndMessageStatus(user, status, pageable);
        }
        return repo.findAllBySubjectUser(user, pageable);
    }

    @Override
    public void readNotification(Long id) {
        Notification notification = getNotification(id).orElseThrow(()-> new ObjectNotFoundException("Notification not found"));
        notification.setMessageStatus(Status.READ.getStatus());
        repo.save(notification);
    }
}
