package com.vgg.fvp.order;

import com.vgg.fvp.common.exceptions.MethodNotAllowedException;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.common.utils.Status;
import com.vgg.fvp.customer.Customer;
import com.vgg.fvp.customer.CustomerRepository;
import com.vgg.fvp.customer.CustomerService;
import com.vgg.fvp.vendor.Menu.Menu;
import com.vgg.fvp.vendor.Menu.MenuService;
import com.vgg.fvp.vendor.Vendor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository repo;
    private CustomerService customerService;
    private MenuService menuService;
    private CustomerRepository customerRepo;
    private OrderService orderService;

    private final LocalDateTime now = LocalDateTime.now();
    private final Long tenMinutes = Long.valueOf(10 * 60 * 1000);

    public OrderServiceImpl(OrderRepository repo, CustomerService customerService, MenuService menuService, CustomerRepository customerRepo, OrderService orderService) {
        this.repo = repo;
        this.customerService = customerService;
        this.menuService = menuService;
        this.customerRepo = customerRepo;
        this.orderService = orderService;
    }

    @Override
    public Orderl createOrder(Customer customer, Long menuId) {
        Orderl order = new Orderl();
        Menu menu = menuService.getMenu(menuId).orElseThrow(() -> new ObjectNotFoundException("Menu Not Found"));
        order.addItem(menu);
        order.setAmountDue(menu.getPrice());
        order.setVendor(menu.getVendor());
        order.setAmountOutstanding(menu.getPrice());
        order.setDescription(menu.getName() + " ordered by customer "+ customer.getFirstName());
        order.setOrderStatus(Status.IN_PROGRESS.getStatus());
        customer.setAmountOutstanding(customer.getAmountOutstanding().add(menu.getPrice()));
        customer = customerRepo.save(customer);
        order.setCustomer(customer);
        System.out.println("This is the customer outstanding payment " + customer.getAmountOutstanding());
        order = repo.save(order);
        return order;
    }

    @Override
    public Orderl addItemToOrder(Customer customer, Long orderId, Long menuId) {
        Orderl order = orderService.getOrderByCustomer(customer,orderId).orElseThrow(() -> new ObjectNotFoundException("Order Not Found"));
        Menu menu = menuService.getMenu(order.getVendor(), menuId).orElseThrow(() -> new ObjectNotFoundException("Menu not found for this Vendor, Consider creating another Order"));
        order.addItem(menu);
        order.setAmountDue(menu.getPrice());
        order.setAmountOutstanding(menu.getPrice());
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
        return repo.save(order) ;
    }


    @Override
    public Orderl cancelOrder(Orderl order) {
        if(isWithin(order.getCreateOn(), now)){
            order.setOrderStatus(Status.CANCELLED.toString());
            return repo.save(order);
        }else
            throw new MethodNotAllowedException("You can not cancel order after 10 minutes");
    }

    @Override
    public Orderl makePayment(Customer customer, Long orderId) {
        return null;
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

    public boolean isWithin(LocalDateTime then, LocalDateTime now){
        Long diff = Duration.between(then,now).toMillis();
        if(diff <= tenMinutes){
            return true;
        }
        return false;
    }
}
