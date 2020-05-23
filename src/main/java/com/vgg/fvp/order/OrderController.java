package com.vgg.fvp.order;

import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.common.utils.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fvp/orders/")
public class OrderController {

    private OrderService orderService;
    private OrderAssembler assembler;

    public OrderController(@Lazy OrderService orderService, OrderAssembler assembler) {
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

    @PostMapping("{id}/make-payment")
    public ResponseEntity makePayment(@PathVariable("id") Long id, @RequestBody PaymentDTO payment){
        Orderl order = orderService.makePayment(id, payment.getAmount());
        return ResponseEntity.ok(new AppResponse("Payment Successful", "success"));
    }


}
