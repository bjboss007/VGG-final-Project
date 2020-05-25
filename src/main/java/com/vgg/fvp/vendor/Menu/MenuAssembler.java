package com.vgg.fvp.vendor.Menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgg.fvp.vendor.Menu.dto.RequestMapperDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MenuAssembler implements RepresentationModelAssembler<Menu, EntityModel<Menu>> {

    private HttpServletRequest request;
    public MenuAssembler(HttpServletRequest request) {
        this.request = request;
    }

    Pageable pageable = PageRequest.of(1,10);

    @Override
    public EntityModel<Menu> toModel(Menu menu) {

        return new EntityModel<>(menu,
                linkTo(methodOn(MenuController.class).getMenu(menu.getVendor().getId() , menu.getId())).withSelfRel(),
                linkTo(methodOn(MenuController.class).getAllMenus(menu.getVendor().getId(), pageable)).withRel("menus"));

    }

    private RequestMapperDTO getMapper(){
        RequestMapperDTO mapper = new ObjectMapper().convertValue(getRequest(), RequestMapperDTO.class);
        return mapper;
    }

    private LinkedHashMap<String, Long> getRequest(){
        return (LinkedHashMap<String, Long>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }


}
