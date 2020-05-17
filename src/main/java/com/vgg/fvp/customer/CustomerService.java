package com.vgg.fvp.customer;


import com.vgg.fvp.customer.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer create(CustomerDTO customer);
    Optional<Customer> getCustomer(Long id);
    Customer update(Long id, Customer customer);
    void delete(Long id);
    Customer addUser(Customer customer, String password);
    Page<Customer> getAllCustomers(Pageable pageable);
    List<Customer> getAllCustomers();
}
