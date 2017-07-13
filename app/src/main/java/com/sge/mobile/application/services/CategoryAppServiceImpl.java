package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.CategoryRepository;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.repositories.CategoryRepositoryImpl;

import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public class CategoryAppServiceImpl implements CategoryAppService {
    private final CategoryRepository categoryRepository;

    public CategoryAppServiceImpl(final SGEDBHelper sgeDBHelper) {
        if (sgeDBHelper == null)
            throw new IllegalArgumentException();

        this.categoryRepository = new CategoryRepositoryImpl(sgeDBHelper);
    }

    @Override
    public Rubro findCategoryByDescription(String description) {
        return this.categoryRepository.getByDescription(description);
    }

    @Override
    public Rubro findCategoryById(int categoryId) {
        return this.categoryRepository.get(categoryId);
    }

    @Override
    public List<Rubro> findCategories() {
        return this.categoryRepository.getAll();
    }

    @Override
    public List<Rubro> findActiveCategories() {
        return this.categoryRepository.getActives();
    }

    @Override
    public void save(Rubro category) {
        this.categoryRepository.store(category);
    }

    @Override
    public void removeAll() {
        this.categoryRepository.removeAll();
    }
}
