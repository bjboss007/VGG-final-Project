package com.vgg.fvp.common;

import com.vgg.fvp.common.data.Role;
import com.vgg.fvp.common.data.RoleRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AppInit implements InitializingBean {

    private RoleRepository repo;

    public AppInit(RoleRepository repo) {
        this.repo = repo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean isPresent = repo.count() > 0;
        if(!isPresent){
            createRoles();
        }
        return;
    }

    public void createRoles(){
        Role customerRole = new Role(Role.UserType.CUSTOMER.getType());
        Role vendorRole = new Role(Role.UserType.VENDOR.getType());
        List<Role> roleList = Arrays.asList(
                customerRole,
                vendorRole
        );
        repo.saveAll(roleList);
    }
}
