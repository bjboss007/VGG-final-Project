package com.vgg.fvp.vendor;

import com.vgg.fvp.vendor.dto.VendorDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VendorService {

    Vendor create(VendorDTO vendor);
    Optional<Vendor> getVendor(Long id);
    Vendor update(Long id, Vendor vendor);
    void delete(Long id);
    Vendor addUser(Vendor vendor, String password);
    Page<Vendor> getAllVendors(Pageable pageable);
    List<Vendor> getAllVendors();
}
