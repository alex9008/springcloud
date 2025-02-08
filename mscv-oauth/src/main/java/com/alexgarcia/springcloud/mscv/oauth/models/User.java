package com.alexgarcia.springcloud.mscv.oauth.models;

import java.util.List;

public class User {


    private Long id;
    private String name;
    private String password;
    private String email;
    private boolean enabled;
    private boolean admin;
    private List<Role> roles;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(Long id, String name, String password, String email, boolean enabled, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles2) {
        this.roles = roles2;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}