package com.vgg.fvp.common.utils;

import com.vgg.fvp.common.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllBySubjectUser(User user);
    Page<Notification> findAllBySubjectUserAndMessageStatus(User user, Optional<String> Status, Pageable pageable);
    Page<Notification> findAllBySubjectUser(User user, Pageable pageable);

}
