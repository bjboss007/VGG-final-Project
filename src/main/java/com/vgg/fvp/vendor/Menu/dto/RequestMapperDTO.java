package com.vgg.fvp.vendor.Menu.dto;

public class RequestMapperDTO {

    private Long vendorId;
    private Long menuId;
    private Long id;
    private Long orderId;

    public RequestMapperDTO() {
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "RequestMapperDTO{" +
                "vendorId=" + vendorId +
                ", menuId=" + menuId +
                ", id=" + id +
                ", orderId=" + orderId +
                '}';
    }
}
