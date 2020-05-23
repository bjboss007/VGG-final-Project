package com.vgg.fvp.common.data;

public interface UserService {
    boolean isCustomer(String email);
    boolean isVendor(String email);
    User createUser(String email, String password, String type);
}
