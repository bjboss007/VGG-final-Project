package com.vgg.fvp.customer;

import com.vgg.fvp.common.data.PasswordDTO;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.order.OrderAssembler;
import com.vgg.fvp.order.OrderService;
import com.vgg.fvp.order.Orderl;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.customer.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fvp/customers/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAssembler assembler;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderAssembler orderAssembler;
    @Autowired
    private PagedResourcesAssembler<Customer> pagedResourcesAssembler;


    @PostMapping("customers/register")
    public ResponseEntity<EntityModel<Customer>> register(@Valid @RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.create(customerDTO);
        EntityModel<Customer> customerEntityModel = assembler.toModel(customer);
        return ResponseEntity.created(customerEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(customerEntityModel);
    }
    
    @GetMapping("{id}")
    public EntityModel<Customer> getCustomer(@PathVariable("id") Long id){
        Customer customer = customerService.getCustomer(id).orElseThrow(() -> new ObjectNotFoundException("Customer Not Found"));
        return assembler.toModel(customer);
    }

    @GetMapping("")
    public ResponseEntity<PagedModel<EntityModel<Customer>>> getAllCustomers (Pageable pageable){
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        PagedModel<EntityModel<Customer>> customerEntities = pagedResourcesAssembler
                .toModel(customers, assembler);
        return ResponseEntity.ok(customerEntities);
    }

    @PostMapping("{id}/set-password")
    public ResponseEntity<RepresentationModel> addUser(@PathVariable("id") Long id, @RequestBody PasswordDTO password){
        Customer existingCustomer = customerService.getCustomer(id).orElseThrow(() -> new ObjectNotFoundException("Customer Not Found"));
        Customer customer = customerService.addUser(existingCustomer, password.getPassword());
        return ResponseEntity.ok(assembler.toModel(customer));
    }

    @PostMapping("{id}/menus/{menuId}/order")
    public ResponseEntity<EntityModel<Orderl>> makeOrder(@PathVariable("id") Long customerId, @PathVariable("menuId") Long menuId ){
        Customer customer = getCustomerDetails(customerId);
        Orderl order = orderService.createOrder(customer, menuId);
        EntityModel<Orderl> orderEntityModel = orderAssembler.toModel(order);
        return ResponseEntity.created(orderEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(orderEntityModel);
    }

    @PostMapping("{id}/orders/{orderId}/add/{menuId}")
    public ResponseEntity<EntityModel<Orderl>> addMoreItems(@PathVariable("id") Long customerId,@PathVariable("orderId") Long orderId, @PathVariable("menuId") Long menuId ){
        Customer customer = getCustomerDetails(customerId);
        Orderl order = orderService.addItemToOrder(customer, orderId, menuId);
        EntityModel<Orderl> orderEntityModel = orderAssembler.toModel(order);
        return ResponseEntity.created(orderEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(orderEntityModel);
    }


    @DeleteMapping("{id}/orders/{orderId}/cancel")
    public ResponseEntity<RepresentationModel> cancelOrder(@PathVariable("id") Long customerId, @PathVariable("orderId") Long orderId){
        Customer customer = getCustomerDetails(customerId);
        Orderl orderl = orderService.getOrderByCustomer(customer, orderId).orElseThrow(() -> new ObjectNotFoundException("Order Not Found"));
        if(orderl.getOrderStatus().equalsIgnoreCase(Status.IN_PROGRESS.getStatus())){
            Orderl order = orderService.cancelOrder(orderl);
            return ResponseEntity.ok(orderAssembler.toModel(order));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + orderl.getOrderStatus() + " status"));
    }

    public Customer getCustomerDetails(Long customerId){
        return customerService.getCustomer(customerId).orElseThrow(() -> new ObjectNotFoundException("Customer Not Found") );
    }

}

