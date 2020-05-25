package com.vgg.fvp.common.utils;


import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/fvp/v1/notifications")
public class NotificationController {

    private NotificationService notificationService;
    private PagedResourcesAssembler<Notification> pagedResourcesAssembler;
    private NotificationAssembler assembler;

    public NotificationController(NotificationService notificationService, PagedResourcesAssembler<Notification> pagedResourcesAssembler, NotificationAssembler assembler) {
        this.notificationService = notificationService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.assembler = assembler;
    }

    @GetMapping("{userId}/all")
    public ResponseEntity getAllNotifications(@PathVariable("userId") Long id,
                                              @RequestParam(required = false, value = "status") Optional<String> status,
                                              Pageable pageable){

        Page<Notification> notifications = notificationService.getNotificationByUserAndStatus(id, status, pageable);
        PagedModel<EntityModel<Notification>> notificationEntities = pagedResourcesAssembler.toModel(notifications, assembler);
        return ResponseEntity.ok(notificationEntities);
    }

    @GetMapping("{id}")
    public  ResponseEntity getOneNotification(@PathVariable("id") Long id){
        Notification notification = notificationService.getNotification(id).orElseThrow(()-> new ObjectNotFoundException("Notification not found"));
        return ResponseEntity.ok(assembler.toModel(notification));
    }
}
