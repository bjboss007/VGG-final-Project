package com.vgg.fvp.vendor.Menu;

import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.vendor.Menu.dto.MenuDTO;
import com.vgg.fvp.vendor.Vendor;
import com.vgg.fvp.vendor.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MenuServiceImpl implements MenuService{

    private MenuRepository repo;
    private VendorService vendorService;

    public MenuServiceImpl(MenuRepository repo, VendorService vendorService) {
        this.repo = repo;
        this.vendorService = vendorService;
    }

    @Override
    public Menu createMenu(Vendor vendor, MenuDTO menuDTO) {

        Menu menu = new Menu();
        menu = mapper(menu,menuDTO);
        menu.setVendor(vendor);
        return repo.save(menu);
    }

    @Override
    public Menu updateMenu(Long id, MenuDTO menuDTO) {
        Menu menu = getMenu(id).orElseThrow(() -> new ObjectNotFoundException("Menu Not found"));
        if(menuDTO.getId() == null) {
            menuDTO.setId(id);
        }
        return repo.save(mapper(menu,menuDTO));
    }

    @Override
    public void deleteMenu(Long vendorId, Long menuId) {

    }

    @Override
    public Optional<Menu> getMenu(Vendor vendor, Long id) {
        return repo.findMenuByVendorAndId(vendor, id);
    }

    @Override
    public Optional<Menu> getMenu(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Menu> getMenusByVendor(Vendor vendor) {
        return repo.findMenusByVendor(vendor);
    }

    @Override
    public Page<Menu> getMenusByVendor(Vendor vendor, Pageable pageable) {
        List<Menu> menuList = getMenusByVendor(vendor);
//        return repo.findMenusByVendor(vendor, pageable);
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > menuList.size() ? menuList.size() : (start + pageable.getPageSize());
        Page<Menu> menus = new PageImpl<Menu>(menuList.subList(start, end), pageable, menuList.size());
        return menus;

    }

    public Menu mapper(Menu menu, MenuDTO menuDTO){
        if(menuDTO.getId() != null)
            menu.setId(menuDTO.getId());
        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setPrice(menuDTO.getPrice());
        menu.setQuantity(menuDTO.getQuantity());
        menu.setFrequencyOfReocurrence(menuDTO.getFrequencyOfReocurrence());
        return menu;
    }
}
