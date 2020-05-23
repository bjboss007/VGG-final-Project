package com.vgg.fvp.vendor;

import com.vgg.fvp.common.data.*;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.order.OrderService;
import com.vgg.fvp.order.Orderl;
import com.vgg.fvp.vendor.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository repo;
    private RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public VendorServiceImpl(VendorRepository repo, RoleRepository roleRepo, PasswordEncoder passwordEncoder, UserService userService, OrderService orderService) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public Vendor create(VendorDTO vendor) {
        return repo.save(mapper(vendor));
    }

    @Override
    public Optional<Vendor> getVendor(Long id) {
        return repo.findById(id);
    }

    @Override
    public Vendor update(Long id, Vendor vendor) {
        Optional<Vendor> existingVendor = getVendor(id);
        if(existingVendor != null){
            vendor.setId(existingVendor.get().getId());
            repo.save(vendor);
        }
        return vendor;
    }

    @Override
    public void delete(Long id) {
        Optional<Vendor> vendor = getVendor(id);
        if(vendor.isPresent()){
            repo.delete(vendor.get());
        }

    }

    @Override
    public Vendor addUser(Vendor vendor, String password) {
        User user = userService.createUser(vendor.getEmail(), password, Role.UserType.VENDOR.getType());
        vendor.setUser(user);
        return repo.save(vendor);
    }

    @Override
    public Page<Vendor> getAllVendors(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return repo.findAll();
    }

    @Override
    public Map<String, Object> generateReport(Vendor vendor) {
        List<Orderl> totalOrders = orderService.getAllOrdersByVendor(vendor);
        List<Orderl> paidOrders = totalOrders.stream().filter(order -> order.getPaymentStatus().equalsIgnoreCase(Status.PAID.getStatus())).collect(Collectors.toList());
        List<Orderl> unpaidOrders = totalOrders.stream().filter(order -> order.getPaymentStatus().equalsIgnoreCase(Status.NOT_PAID.getStatus())).collect(Collectors.toList());
        BigDecimal amountPaid = paidOrders.stream().map(Orderl::getAmountDue).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amountNotPaid = unpaidOrders.stream().map(Orderl::getAmountDue).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> report = new HashMap<>();
        report.put("Vendor Name",vendor.getBusinessName());
        report.put("Total sales count", totalOrders.size());
        report.put("Total paid count", paidOrders.size());
        report.put("Total amount sales paid", amountPaid );
        report.put("Total amount sales not paid", amountNotPaid );
        return report;
    }

    public Vendor mapper(VendorDTO vendorDTO){
        Vendor vendor = new Vendor();
        vendor.setBusinessName(vendorDTO.getBusinessName());
        vendor.setEmail(vendorDTO.getEmail());
        vendor.setPhoneNumber(vendorDTO.getPhoneNumber());
        return vendor;
    }

}
