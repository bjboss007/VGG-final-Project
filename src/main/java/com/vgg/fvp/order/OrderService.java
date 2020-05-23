package com.vgg.fvp.order;

import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.vendor.Vendor;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Orderl createOrder(Customer customer,Long menuId);
    Orderl addItemToOrder(Customer customer, Long order, Long menuId);
    Orderl UpdateOrder(Long orderId);
    Orderl UpdateOrderStatus(Orderl order);
    Orderl cancelOrder(Orderl orderl);
    Orderl makePayment(Long orderId, BigDecimal amount);
    Optional<Orderl> getOrderByCustomer(Customer customer, Long orderId);
    Optional<Orderl> getOrderByVendor(Vendor vendor, Long orderId);
    Optional<Orderl> getOrder(Long id);
    List<Orderl> getAllOrders();
    List<Orderl> getAllOrdersByVendor(Vendor vendor);
    Page<Orderl> getAllOrdersByVendor(Vendor vendor, Pageable pageable);

}
