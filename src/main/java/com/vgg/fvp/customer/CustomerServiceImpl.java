package com.vgg.fvp.customer;

import com.vgg.fvp.common.data.Role;
import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.exceptions.BadRequestException;
import com.vgg.fvp.customer.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repo;
    private UserService userService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Override
    public Customer create(CustomerDTO customer) {
        Customer customer1 = mapper(customer);
        return repo.save(customer1);
    }

    @Override
    public Optional<Customer> getCustomer(Long id) {
        return repo.findById(id);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        Optional<Customer> existingCustomer = getCustomer(id);
        if(existingCustomer != null){
            customer.setId(existingCustomer.get().getId());
            repo.save(customer);
        }
        return customer;
    }

    @Override
    public void delete(Long id) {
        Optional<Customer> customer = getCustomer(id);
        if (customer.isPresent())
            repo.delete(customer.get());
    }

    @Override
    public Customer addUser(Customer customer, String password) {
        if(customer.getUser() == null){
            User user = userService.createUser(customer.getEmail(), password, Role.UserType.CUSTOMER.getType());
            customer.setUser(user);
            return repo.save(customer);
        }
        throw new BadRequestException("Customer already has a user");
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public Customer mapper(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        return customer;
    }

}
