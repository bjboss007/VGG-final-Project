package com.vgg.fvp.vendor;

import com.vgg.fvp.common.data.PasswordDTO;
import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.customer.CustomerController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class VendorAssembler implements RepresentationModelAssembler<Vendor, EntityModel<Vendor>> {

    Pageable pageable = PageRequest.of(1,10);
    @Override
    public EntityModel<Vendor> toModel(Vendor vendor) {
        EntityModel<Vendor> vendorResource = new EntityModel<>(vendor,
                linkTo(methodOn(VendorController.class).getVendor(vendor.getId())).withSelfRel(),
                linkTo(methodOn(VendorController.class).getAllVendors(pageable)).withRel("vendors"));

        if(vendor.getUser() == null){
            PasswordDTO password = new PasswordDTO();
            vendorResource.add(
                    linkTo(methodOn(VendorController.class).addUser(vendor.getId(), password)).withRel("set-password")
            );
        }
        return vendorResource;
    }

    @Override
    public CollectionModel<EntityModel<Vendor>> toCollectionModel(Iterable<? extends Vendor> entities) {
        return null;
    }
}
