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

    List<Mesa> getTablesArray(String serviceUrl);

    boolean sendOrder(int waiter, int table, String order, String notes, String serviceUrl);

    boolean testConnection(String serviceUrl);

    boolean changeUserPassword(String user, String oldPassword, String newPassword, String serviceUrl);

    ResumenMesa getTableStatus(int waiter, int table, String serviceUrl);

    List<Sector> getSectorsForWaiter(int waiter, String serviceUrl);
}
