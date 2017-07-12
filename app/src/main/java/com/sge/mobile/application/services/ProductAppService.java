package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.Producto;

/**
 * Created by Daniel on 03/04/14.
 */
public interface ProductAppService {
    Producto findProductByDescription(String description);

    Producto findProductById(int productId);

    void save(Producto product);

    void saveAccessorie(Accesorio accessorie);

    void removeAll();
}
