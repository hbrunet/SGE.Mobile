package com.sge.mobile.presentation;

import com.sge.mobile.domain.model.Pedido;

/**
 * Created by Daniel on 05/04/14.
 */
public final class UserSession {
    private final static UserSession instance;

    static {
        instance = new UserSession();
    }

    private int userId;
    private Pedido order;
    private int tablesNumber;
    private String sgeServiceUrl;
    private Boolean administrator;
    private String userName;
    private String password;

    public static UserSession getInstance() {
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Pedido getOrder() {
        if (this.order == null)
            this.order = new Pedido();
        return order;
    }

    public void setOrder(Pedido order) {
        this.order = order;
    }

    public int getTablesNumber() {
        return tablesNumber;
    }

    public void setTablesNumber(int tablesNumber) {
        this.tablesNumber = tablesNumber;
    }

    public String getSgeServiceUrl() {
        return sgeServiceUrl;
    }

    public void setSgeServiceUrl(String sgeServiceUrl) {
        this.sgeServiceUrl = sgeServiceUrl;
    }

    public Boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
