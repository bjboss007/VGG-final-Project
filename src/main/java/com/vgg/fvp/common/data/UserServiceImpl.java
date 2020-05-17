package com.vgg.fvp.common.data;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
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
}
