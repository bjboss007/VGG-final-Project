package com.vgg.fvp.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificationAssembler implements RepresentationModelAssembler<Notification, EntityModel<Notification>> {

    @Override
    public EntityModel<Notification> toModel(Notification notification) {
        Optional<String> status = Optional.of("");
        PageRequest page = PageRequest.of(1,20);
        return new EntityModel<>(notification,
                linkTo(methodOn(NotificationController.class).getOneNotification(notification.getId())).withSelfRel(),
                linkTo(methodOn(NotificationController.class).getAllNotifications(notification.getSubjectUser().getId(), status, page)).withRel("all-notifications")
        );
    }
}
