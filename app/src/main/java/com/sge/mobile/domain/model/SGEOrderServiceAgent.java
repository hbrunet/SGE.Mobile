package com.sge.mobile.domain.model;

import com.sge.mobile.application.services.CategoryAppService;
import com.sge.mobile.application.services.ProductAppService;

import java.util.List;

/**
 * Created by Daniel on 04/04/14.
 */
public interface SGEOrderServiceAgent {
    int logIn(String user, String password, String serviceUrl);

    List<Rubro> synchcronizeCategories(String serviceUrl);

    List<Producto> synchronizeProducts(CategoryAppService categoryAppService, String serviceUrl);

    List<Accesorio> synchronizeAccessories(ProductAppService productAppService, String serviceUrl);

    void logOut(int user, String serviceUrl);

    int getTables(String serviceUrl);

    void sendOrder(int waiter, int table, String order, String notes, String serviceUrl);

    boolean testConnection(String serviceUrl);

    void getTableOrders(int table);

    void changeUserPassword(String user, String oldPassword, String newPassword);
}
