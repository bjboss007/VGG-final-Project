package com.vgg.fvp.vendor;

import com.vgg.fvp.common.data.Role;
import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.exceptions.BadRequestException;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.order.OrderService;
import com.vgg.fvp.order.Orderl;
import com.vgg.fvp.vendor.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository repo;
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public VendorServiceImpl(VendorRepository repo, UserService userService, OrderService orderService) {
        this.repo = repo;
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
        if (vendor.getUser() == null){
            User user = userService.createUser(vendor.getEmail(), password, Role.UserType.VENDOR.getType());
            vendor.setUser(user);
            return repo.save(vendor);
        }
        throw new BadRequestException("Vendor already has a user");
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
        List<Orderl> totalOrders = orderService.getAllOrdersByVendor(vendor).stream().filter(order -> order.getCreateOn().toLocalDate().isEqual(LocalDate.now())).collect(Collectors.toList());
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
