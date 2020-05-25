package com.vgg.fvp.common.data;

import com.vgg.fvp.common.exceptions.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repo;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepo;
    private UserRepository userRepo;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder, RoleRepository roleRepo, UserRepository userRepo) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isCustomer(String email) {
        User user = repo.findUserByEmail(email);
        return user.getRole().getName().equals(Role.UserType.CUSTOMER.getType());
    }

    @Override
    public boolean isVendor(String email) {
        User user = repo.findUserByEmail(email);
        return user.getRole().getName().equals(Role.UserType.VENDOR.getType());
    }

    @Override
    public User createUser(String email, String password, String type) {
        User existingUser = repo.findUserByEmail(email);
        if(existingUser == null){
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepo.findRoleByName(Role.UserType.CUSTOMER.getType());
            user.setRole(role);
            userRepo.save(user);
            return user;
        }
        throw new BadRequestException("User already exist with the email");
    }

    @Override
    public Optional<User> getUser(Long id) {
        return repo.findById(id);
    }
}
