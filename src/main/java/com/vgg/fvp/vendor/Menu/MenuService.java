package com.vgg.fvp.vendor.Menu;

import com.vgg.fvp.vendor.Menu.dto.MenuDTO;
import com.vgg.fvp.vendor.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    Menu createMenu(Vendor vendor, MenuDTO menuDTO);
    Menu updateMenu(Long id, MenuDTO menuDTO);
    void deleteMenu(Long vendorId, Long menuId);
    Optional<Menu> getMenu(Vendor vendor, Long id);
    Optional<Menu> getMenu(Long id);
    List<Menu> getMenusByVendor(Vendor vendor);
    Page<Menu> getMenusByVendor(Vendor vendor, Pageable pageable);
}
