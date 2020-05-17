package com.vgg.fvp.customer;

import com.vgg.fvp.common.data.Role;
import com.vgg.fvp.common.data.RoleRepository;
import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserRepository;
import com.vgg.fvp.customer.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repo;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepo;
    private RoleRepository roleRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repo, PasswordEncoder passwordEncoder, UserRepository userRepo, RoleRepository roleRepo) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
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
        User user = createUser(customer.getEmail(), password);
        customer.setUser(user);
        return repo.save(customer);
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

    public User createUser(String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepo.findRoleByName(Role.UserType.CUSTOMER.getType());
        user.setRole(role);
        userRepo.save(user);
        return user;
    }
}
