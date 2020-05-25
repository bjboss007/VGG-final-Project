package com.vgg.fvp.common.data;

import java.util.Optional;

public interface UserService {
    boolean isCustomer(String email);
    boolean isVendor(String email);
    User createUser(String email, String password, String type);
    Optional<User> getUser(Long id);
}
