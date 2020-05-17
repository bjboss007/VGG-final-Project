package com.vgg.fvp.order;

import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.customer.CustomerController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fvp/orders/")
public class OrderController {

    private OrderService orderService;
    private OrderAssembler assembler;

    public OrderController(OrderService orderService, OrderAssembler assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @GetMapping("{orderId}")
    public EntityModel<Orderl> getOrder(@PathVariable("orderId") Long orderId){
        Orderl order = orderService.getOrder(orderId).orElseThrow(() -> new ObjectNotFoundException("Order Not Found"));
        return assembler.toModel(order);
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Orderl>> getAllOrders(){
        List<EntityModel<Orderl>> orders = orderService.getAllOrders().stream()
                .map(order -> assembler.toModel(order)).collect(Collectors.toList());
        return new CollectionModel<>(orders,
                linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }
}
