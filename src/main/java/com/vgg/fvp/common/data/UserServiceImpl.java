package com.vgg.fvp.common.data;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (user.getRole().getName() == Role.UserType.CUSTOMER.getType()){
            return true;
        }
        return false;
    }

    @Override
    public boolean isVendor(String email) {
        User user = repo.findUserByEmail(email);
        if (user.getRole().getName() == Role.UserType.VENDOR.getType()){
            return true;
        }
        return false;
    }

    @Override
    public User createUser(String email, String password, String type) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepo.findRoleByName(Role.UserType.CUSTOMER.getType());
        user.setRole(role);
        userRepo.save(user);
        return user;
    }
}
