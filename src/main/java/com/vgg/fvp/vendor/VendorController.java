package com.vgg.fvp.vendor;

import com.vgg.fvp.common.data.PasswordDTO;
import com.vgg.fvp.common.exceptions.BadRequestException;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.customer.CustomerController;
import com.vgg.fvp.order.OrderAssembler;
import com.vgg.fvp.order.OrderService;
import com.vgg.fvp.order.Orderl;
import com.vgg.fvp.vendor.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fvp/vendors/")
public class VendorController {

    private VendorService vendorService;
    private VendorAssembler assembler;
    private OrderAssembler orderAssembler;
    private OrderService orderService;
    private PagedResourcesAssembler<Vendor> pagedResourcesAssembler;
    private PagedResourcesAssembler<Orderl> orderlPagedResourcesAssembler;

    @Autowired
    public VendorController(VendorService vendorService, VendorAssembler assembler, OrderAssembler orderAssembler, OrderService orderService, PagedResourcesAssembler pagedResourcesAssembler, PagedResourcesAssembler<Orderl> orderlPagedResourcesAssembler) {
        this.vendorService = vendorService;
        this.assembler = assembler;
        this.orderAssembler = orderAssembler;
        this.orderService = orderService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.orderlPagedResourcesAssembler = orderlPagedResourcesAssembler;
    }

    @PostMapping("register")
    public ResponseEntity<EntityModel<Vendor>> register(@Valid @RequestBody VendorDTO vendorDTO){
        Vendor vendor = vendorService.create(vendorDTO);
        EntityModel<Vendor> vendorEntityModel = assembler.toModel(vendor);
        return ResponseEntity.created(vendorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(vendorEntityModel);
    }

    @GetMapping("{id}")
    public EntityModel<Vendor> getVendor(@PathVariable("id") Long id){
        Vendor vendor = vendorService.getVendor(id).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        return assembler.toModel(vendor);
    }

//    @GetMapping("")
//    public CollectionModel<EntityModel<Vendor>> getAllVendors (){
//        List<EntityModel<Vendor>> vendors = vendorService.getAllVendors().stream()
//                .map(vendor -> assembler.toModel(vendor))
//                .collect(Collectors.toList());
//        return new CollectionModel<>(vendors,
//                linkTo(methodOn(VendorController.class).getAllVendors()).withSelfRel());
//    }
    @GetMapping("")
    public ResponseEntity<PagedModel<EntityModel<Vendor>>> getAllVendors (Pageable pageable){
        Page<Vendor> vendors = vendorService.getAllVendors(pageable);
        PagedModel<EntityModel<Vendor>> vendorEntities = pagedResourcesAssembler
                .toModel(vendors, assembler);
        return ResponseEntity.ok(vendorEntities);
    }

    @PostMapping("{id}/set-password")
    public ResponseEntity<RepresentationModel> addUser(@PathVariable("id") Long id, @RequestBody PasswordDTO password){
        Vendor existingVendor = vendorService.getVendor(id).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        Vendor vendor = vendorService.addUser(existingVendor, password.getPassword());
        return ResponseEntity.ok(assembler.toModel(vendor));
    }

    @PutMapping("{vendorId}/orders/{orderId}/updateStatus")
    public ResponseEntity<RepresentationModel> updateOrderStatus(@PathVariable("vendorId") Long id, @PathVariable("orderId") Long orderId){
        Vendor vendor = vendorService.getVendor(id).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        Orderl order = orderService.getOrderByVendor(vendor, orderId).orElseThrow(() -> new ObjectNotFoundException("Order Not Found"));
        if(order.getOrderStatus().equalsIgnoreCase(Status.IN_PROGRESS.getStatus()) && !order.getOrderStatus().equalsIgnoreCase(Status.CANCELLED.getStatus()) ) {
            Orderl order2 = orderService.UpdateOrderStatus(order);
            return ResponseEntity.ok(orderAssembler.toModel(order2));
        }
        throw new BadRequestException("You can not update a completed order");
    }

//    @GetMapping("{vendorId}/orders")
//    public CollectionModel<EntityModel<Orderl>> getAllOrders(@PathVariable("vendorId") Long vendorId, Pageable pa){
//        Vendor vendor = vendorService.getVendor(vendorId).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
//        Page<Orderl> orders = orderService.getAllOrdersByVendor(vendor, pa);
//        List<EntityModel<Orderl>> allOrders = orderService.getAllOrdersByVendor(vendor).stream()
//                .map(order -> orderAssembler.toModel(order))
//                .collect(Collectors.toList());
//
////        return new CollectionModel<>(allOrders, linkTo(methodOn(VendorController.class).getAllOrders(vendorId)).withSelfRel());
//    }
//
    @GetMapping("{vendorId}/orders")
    public ResponseEntity<PagedModel<EntityModel<Orderl>>> getAllOrders(@PathVariable("vendorId") Long vendorId, Pageable page){
        Vendor vendor = vendorService.getVendor(vendorId).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        Page<Orderl> orders = orderService.getAllOrdersByVendor(vendor, page);
        PagedModel<EntityModel<Orderl>> orderEntities = orderlPagedResourcesAssembler
                .toModel(orders, orderAssembler);
        return  ResponseEntity.ok(orderEntities);
    }

    @GetMapping("{vendorId}/report")
    public ResponseEntity getReport(@PathVariable("vendorId") Long vendorId){
        Vendor vendor = vendorService.getVendor(vendorId).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        Map<String, Object> report = vendorService.generateReport(vendor);
        return ResponseEntity.ok(report);
    }

}
