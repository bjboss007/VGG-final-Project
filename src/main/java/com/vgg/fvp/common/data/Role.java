package com.vgg.fvp.common.data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Role extends AbstractEntity {

    @NotNull
    @NotBlank
    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }

    public enum UserType{
        VENDOR("VENDOR"), CUSTOMER("CUSTOMER");
        private final String type;

        public String getType() {
            return type;
        }
        UserType(String t){this.type = t;}
    }
}
