package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.AccessoryRepository;
import com.sge.mobile.domain.model.ProductRepository;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.repositories.AccessoryRepositoryImpl;
import com.sge.mobile.infrastructure.data.repositories.ProductRepositoryImpl;

import java.util.List;

/**
 * Created by Daniel on 03/04/14.
 */
public class ProductAppServiceImpl implements ProductAppService {
    private final ProductRepository productRepository;
    private final AccessoryRepository accessoryRepository;

    public ProductAppServiceImpl(final SGEDBHelper sgeDBHelper) {
        if (sgeDBHelper == null)
            throw new IllegalArgumentException();

        this.productRepository = new ProductRepositoryImpl(sgeDBHelper);
        this.accessoryRepository = new AccessoryRepositoryImpl(sgeDBHelper);
    }

    @Override
    public Producto findProductByDescription(String description) {
        return this.productRepository.GetByDescription(description);
    }

    @Override
    public Producto findProductById(int productId) {
        return this.productRepository.get(productId);
    }

    @Override
    public void save(Producto product) {
        this.productRepository.store(product);
    }

    @Override
    public void saveAccessorie(Accesorio accessorie) {
        this.productRepository.storeAccessorie(accessorie);
    }

    @Override
    public void removeAll() {
        this.productRepository.removeAll();
    }

    @Override
    public List<Accesorio> FindAccessories() {
        return this.accessoryRepository.getAll();
    }

    @Override
    public List<Producto> FindProducts(String searchText) {
        return this.productRepository.getFiltered(searchText);
    }
}
