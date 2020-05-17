package com.vgg.fvp.customer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class CustomerAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    Pageable pageable = PageRequest.of(1,10);
    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        EntityModel<Customer> customerResource = new EntityModel<>(customer,
                linkTo(methodOn(CustomerController.class).getCustomer(customer.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers(pageable)).withRel("customers"));


        if(customer.getUser() == null){
            CustomerController.Password password = new CustomerController.Password();
            customerResource.add(
                    linkTo(methodOn(CustomerController.class).addUser(customer.getId(), password)).withRel("set-password")
            );
        }

        return customerResource;
    }

    @Override
    public CollectionModel<EntityModel<Customer>> toCollectionModel(Iterable<? extends Customer> entities) {
        return null;
    }
}
