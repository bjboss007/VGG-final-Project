package com.vgg.fvp.common.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    boolean isCustomer(String email);
    boolean isVendor(String email);
    User createUser(String email, String password, String type);
    Optional<User> getUser(Long id);
    Page<User> getAllUsers(Pageable pageable);
}
