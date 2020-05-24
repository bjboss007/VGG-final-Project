package com.vgg.fvp.order;

import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.exceptions.BadRequestException;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.common.smtp.EmailService;
import com.vgg.fvp.common.utils.Notification;
import com.vgg.fvp.common.utils.NotificationRepository;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.customer.CustomerRepository;
import com.vgg.fvp.vendor.Menu.Menu;
import com.vgg.fvp.vendor.Menu.MenuService;
import com.vgg.fvp.vendor.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repo;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationRepository notificationRepo;

    private final LocalDateTime now = LocalDateTime.now();
    private final Long tenMinutes = Long.valueOf(10 * 60 * 1000);

    @Override
    public Orderl createOrder(Customer customer, Long menuId) {
        Orderl order = new Orderl();
        Menu menu = menuService.getMenu(menuId).orElseThrow(() -> new ObjectNotFoundException("Menu Not Found"));
        order.addItem(menu);
        order.setPaymentStatus(Status.NOT_PAID.getStatus());
        order.setAmountDue(menu.getPrice());
        order.setVendor(menu.getVendor());
        order.setAmountOutstanding(menu.getPrice());
        order.setDescription(menu.getName() + " ordered by customer "+ customer.getFirstName());
        order.setOrderStatus(Status.IN_PROGRESS.getStatus());
        customer.setAmountOutstanding(customer.getAmountOutstanding().add(menu.getPrice()));
        customer = customerRepo.save(customer);
        order.setCustomer(customer);
        order = repo.save(order);
        sendOrderSuccessful(menu.getVendor().getEmail(), customer.getEmail());
        String message = "Customer " + customer.getFullName() + "just placed an order on " + menu.getName();
        createNotification(menu.getVendor().getUser(), message);
        return order;
    }

    @Override
    public Orderl addItemToOrder(Customer customer, Long orderId, Long menuId) {
        Orderl order = getOrderByCustomer(customer,orderId).orElseThrow(() -> new ObjectNotFoundException("Order Not Found"));
        Menu menu = menuService.getMenu(order.getVendor(), menuId).orElseThrow(() -> new ObjectNotFoundException("Menu not found for this Vendor, Consider creating another Order"));
        order.addItem(menu);
        order.setAmountDue(order.getAmountDue().add(menu.getPrice()));
        order.setAmountOutstanding(order.getAmountOutstanding().add(menu.getPrice()));
        customer.setAmountOutstanding(customer.getAmountOutstanding().add(menu.getPrice()));
        customer = customerRepo.save(customer);
        order.setCustomer(customer);
        return order;
    }

    @Override
    public Orderl UpdateOrder(Long orderId) {
        return null;
    }

    @Override
    public Orderl UpdateOrderStatus(Orderl order) {
        order.setOrderStatus(Status.DELIVERED.getStatus());
        sendOrderPickUp(order.getVendor().getEmail(), order.getCustomer().getEmail());
        String message = "Your order of "+ order.getItemsOrdered().get(0).getName() + "is ready";
        createNotification(order.getCustomer().getUser(), message);
        return repo.save(order) ;
    }

    @Override
    public Orderl cancelOrder(Orderl order) {
        String username = loggedInUsername();
        if(userService.isCustomer(username) && order.getCustomer().getEmail().equalsIgnoreCase(username)){
            if(isWithin(order.getCreateOn(), this.now)){
                order.setOrderStatus(Status.CANCELLED.toString());
                String message = "Order of has been cancelled by " + order.getCustomer();
                createNotification(order.getVendor().getUser(), message);
                return repo.save(order);
            }else
                throw new BadRequestException("You can not cancel order after 10 minutes");
        }else {
            throw new BadRequestException("You can not cancel order for another user");
        }

    }

    @Override
    public Orderl makePayment(Long orderId, BigDecimal amount) {
        Orderl order = getOrder(orderId).orElseThrow(() -> new ObjectNotFoundException("Order not found"));
        String username = loggedInUsername();
        if(userService.isCustomer(username) && order.getCustomer().getEmail().equalsIgnoreCase(username)){
            order.getAmountOutstanding().subtract(amount);
            order.setPaymentStatus(Status.PAID.getStatus());
            Customer customer = order.getCustomer();
            customer.getAmountOutstanding().subtract(amount);
            repo.save(order);
            customerRepo.save(customer);
        }else {
            throw new BadRequestException("You can not make payment payment for another user");
        }
        return order;
    }

    @Override
    public Optional<Orderl> getOrderByCustomer(Customer customer, Long orderId) {
        return Optional.ofNullable(repo.findByCustomerAndId(customer, orderId));
    }

    @Override
    public Optional<Orderl> getOrderByVendor(Vendor vendor, Long orderId) {
        return Optional.ofNullable(repo.findByVendorAndId(vendor, orderId));
    }

    @Override
    public Optional<Orderl> getOrder(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Orderl> getAllOrders() {
        return repo.findAll();
    }

    @Override
    public List<Orderl> getAllOrdersByVendor(Vendor vendor) {
        return repo.findAllByVendor(vendor);
    }

    @Override
    public Page<Orderl> getAllOrdersByVendor(Vendor vendor, Pageable pageable) {
        return repo.findAllByVendor(vendor, pageable);
    }

    public boolean isWithin(LocalDateTime then, LocalDateTime now){
        long diff = Duration.between(then,now).toMillis();
        return diff <= this.tenMinutes;
    }

    public String loggedInUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    public void sendOrderSuccessful(String from, String to){
        String[] toEmail = {to};
        String subject = "Order Successfully booked";
        String title = "Your Order is successful";
        String details = "Thank You for patronizing us, Your order will be ready for pick up in 10minutes";
        emailService.sendMail(from, toEmail, subject, title, details);
    }

    public void sendOrderPickUp(String from, String to){
        String[] toEmail = {to};
        String subject = "Your Order is ready";
        String title = "Your Order is ready";
        String details = "Thank You for patronizing us, Your order is ready for pick up";
        emailService.sendMail(from, toEmail, subject, title, details);
    }

    public void createNotification(User user, String message){
        Notification n = new Notification();
        n.setSubjectUser(user);
        n.setMessage(message);
        n.setMessageStatus(Status.SENT.getStatus());
        notificationRepo.save(n);
    }
}
