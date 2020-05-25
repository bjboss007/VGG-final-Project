package com.vgg.fvp.common.utils;

import com.vgg.fvp.common.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Optional<Notification> getNotification(Long id);
    void deleteNotification(Long id);
    void readNotification(Long id);
    List<Notification> notifications(User user);
    Page<Notification> getNotificationByUserAndStatus(Long userId, Optional<String> status, Pageable pageable);

}
