package com.sge.mobile.domain.model;

import com.sge.mobile.domain.core.Repository;

import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public interface CategoryRepository extends Repository<Rubro> {
    Rubro getByDescription(String description);

    List<Rubro> getActives();
}
