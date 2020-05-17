package com.vgg.fvp.vendor.Menu;

import com.vgg.fvp.vendor.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findMenusByVendor(Vendor vendor);
    Optional<Menu> findMenuByVendorAndId(Vendor vondor, Long id);
    Page<Menu> findMenusByVendor(Vendor vendor, Pageable pageable);
}
