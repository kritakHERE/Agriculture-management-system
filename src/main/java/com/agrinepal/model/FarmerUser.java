package com.agrinepal.model;

public class FarmerUser extends User {
    private static final long serialVersionUID = 1L;
    private String farmerId;

    public FarmerUser(String id, String username, String password, String fullName, String farmerId) {
        super(id, username, password, fullName);
        this.farmerId = farmerId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    @Override
    public UserRole getRole() {
        return UserRole.FARMER;
    }
}
