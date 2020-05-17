package com.vgg.fvp.vendor.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
public class VendorDTO {

    @NotNull(message = "Business name must not be null")
    private String businessName;
    @Email
    @NotNull(message = "Email must not be null")
    private String email;
    @NotNull(message = "Phone Number must not be null")
    private String phoneNumber;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
