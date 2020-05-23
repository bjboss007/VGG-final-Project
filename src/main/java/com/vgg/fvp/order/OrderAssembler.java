package com.vgg.fvp.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.customer.CustomerController;
import com.vgg.fvp.vendor.Menu.dto.RequestMapperDTO;
import com.vgg.fvp.vendor.VendorController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<Orderl, EntityModel<Orderl>> {

    private UserService userService;
//    private HttpServletRequest request;
    public OrderAssembler(UserService userService) {
        this.userService = userService;
//        this.request = request;
    }

    Pageable pageable = PageRequest.of(1,10);
    @Override
    public EntityModel<Orderl> toModel(Orderl order) {
        EntityModel<Orderl> orderResource = new EntityModel<>(order,
                linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders")
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PaymentDTO payment = new PaymentDTO();
        if (!auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            if (userService.isCustomer(auth.getPrincipal().toString())) {
                orderResource.add(
                        linkTo(methodOn(OrderController.class).makePayment(order.getId(), payment)).withRel("make-payment")
                );
                if (order.getOrderStatus().equalsIgnoreCase(Status.IN_PROGRESS.getStatus()))
                    orderResource.add(
                            linkTo(methodOn(CustomerController.class).cancelOrder(order.getCustomer().getId(), order.getId())).withRel("cancel"));
            }
            if (userService.isVendor(auth.getPrincipal().toString())){
                orderResource.add(
                        linkTo(methodOn(VendorController.class).updateOrderStatus(order.getVendor().getId(), order.getId())).withRel("complete"),
                        linkTo(methodOn(VendorController.class).getAllOrders(order.getVendor().getId(), pageable)).withRel("all-orders")
                );
            }
        }
        return orderResource;
    }

//    private RequestMapperDTO getMapper(){
//        RequestMapperDTO mapper = new ObjectMapper().convertValue(getRequest(), RequestMapperDTO.class);
//        return mapper;
//    }
//
//    private LinkedHashMap<String, Long> getRequest(){
//        return (LinkedHashMap<String, Long>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//    }
}
