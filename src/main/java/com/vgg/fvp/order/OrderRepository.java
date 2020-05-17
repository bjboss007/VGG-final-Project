package com.vgg.fvp.order;

import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.vendor.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orderl, Long> {
    List<Orderl> findAllByCustomer(Customer customer);
    List<Orderl> findAllByVendor(Vendor vendor);
    Orderl findByCustomerAndId(Customer customer, Long id);
    Orderl findByVendorAndId(Vendor vendor, Long orderId);
}
