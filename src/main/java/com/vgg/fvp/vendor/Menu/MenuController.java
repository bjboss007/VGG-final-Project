package com.vgg.fvp.vendor.Menu;


import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import com.vgg.fvp.vendor.Menu.dto.MenuDTO;
import com.vgg.fvp.vendor.Vendor;
import com.vgg.fvp.vendor.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/fvp/v1/vendors/")
public class MenuController {

    private MenuService menuService;
    private VendorService vendorService;
    private MenuAssembler assembler;

    private PagedResourcesAssembler<Menu> pagedResourcesAssembler;

    public MenuController(MenuService menuService, VendorService vendorService, MenuAssembler assembler, PagedResourcesAssembler<Menu> pagedResourcesAssembler) {
        this.menuService = menuService;
        this.vendorService = vendorService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping("{vendorId}/menus")
    public ResponseEntity<?> createMenu(@PathVariable("vendorId") Long id, @Valid @RequestBody MenuDTO menuDTO){
        Vendor vendor = getVendor(id);
        Menu menu = menuService.createMenu(vendor,menuDTO);
        EntityModel<Menu> menuEntityModel = assembler.toModel(menu);
        return ResponseEntity.created(menuEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(menuEntityModel);
    }

    @PutMapping("{vendorId}/menus/{menuId}/update")
    public ResponseEntity<?> update(@PathVariable("vendorId") Long id, @PathVariable("menuId") Long menuId, @Valid @RequestBody MenuDTO menuDTO){
        Vendor vendor = getVendor(id);
        Menu menu = menuService.updateMenu(menuId,menuDTO);
        EntityModel<Menu> menuEntityModel = assembler.toModel(menu);
        return ResponseEntity.created(menuEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(menuEntityModel);
    }

    @GetMapping("{vendorId}/menus/{menuId}")
    public EntityModel<Menu> getMenu(@PathVariable("vendorId") Long id, @PathVariable("menuId") Long menuId){
        Vendor vendor = getVendor(id);
        Menu menu = menuService.getMenu(vendor,menuId).orElseThrow(()->new ObjectNotFoundException("Menu Not Found"));
        return assembler.toModel(menu);
    }


    @GetMapping("{vendorId}/menus")
    public ResponseEntity<PagedModel<EntityModel<Menu>>> getAllMenus(@PathVariable("vendorId") Long id, Pageable pageable){
        Vendor vendor = getVendor(id);
        Page<Menu> menus = menuService.getMenusByVendor(vendor, pageable);
        PagedModel<EntityModel<Menu>> menuEntities = pagedResourcesAssembler
                .toModel(menus, assembler);
        return ResponseEntity.ok(menuEntities);
    }

    public Vendor getVendor(Long id){
        Vendor vendor = vendorService.getVendor(id).orElseThrow(() -> new ObjectNotFoundException("Vendor Not Found"));
        return vendor;
    }

}
