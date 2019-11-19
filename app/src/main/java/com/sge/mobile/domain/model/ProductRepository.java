package com.sge.mobile.domain.model;

import com.sge.mobile.domain.core.Repository;

import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public interface ProductRepository extends Repository<Producto> {
    Producto GetByDescription(String description);

    void storeAccessorie(Accesorio accessorie);

    void removeAll();

    List<Producto> getFiltered(String searchText);
}
