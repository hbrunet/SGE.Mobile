package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Rubro;

import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public interface CategoryAppService {
    Rubro findCategoryByDescription(String description);

    Rubro findCategoryById(int categoryId);

    List<Rubro> findCategories();

    List<Rubro> findActiveCategories();

    void save(Rubro category);
}
