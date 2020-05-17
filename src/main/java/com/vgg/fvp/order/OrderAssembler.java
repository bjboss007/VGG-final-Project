package com.vgg.fvp.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.security.UserPrincipal;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.customer.CustomerController;
import com.vgg.fvp.vendor.Menu.dto.RequestMapperDTO;
import com.vgg.fvp.vendor.Vendor;
import com.vgg.fvp.vendor.VendorController;
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
    private HttpServletRequest request;
    public OrderAssembler(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @Override
    public EntityModel<Orderl> toModel(Orderl order) {
        EntityModel<Orderl> orderResource = new EntityModel<>(order,
                linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders")
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            UserPrincipal user = (UserPrincipal) auth.getDetails();

            if (userService.isCustomer(user.getUsername()))
                if (order.getOrderStatus() == Status.IN_PROGRESS.getStatus())
                    orderResource.add(
                            linkTo(methodOn(CustomerController.class).cancelOrder(getMapper().getId(), order.getId())).withRel("cancel"));


            if (userService.isVendor(user.getUsername()))
                orderResource.add(
                        linkTo(methodOn(VendorController.class).updateOrderStatus(getMapper().getVendorId(), order.getId())).withRel("complete"),
                        linkTo(methodOn(VendorController.class).getAllOrders(getMapper().getVendorId())).withRel("all-orders")
                );
        }
        return orderResource;
    }

    private RequestMapperDTO getMapper(){
        RequestMapperDTO mapper = new ObjectMapper().convertValue(getRequest(), RequestMapperDTO.class);
        return mapper;
    }

    private LinkedHashMap<String, Long> getRequest(){
        return (LinkedHashMap<String, Long>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }
}
