package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Configuracion;
import com.sge.mobile.domain.model.ConfigurationRepository;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.repositories.ConfigurationRepositoryImpl;

/**
 * Created by Daniel on 10/04/14.
 */
public class ConfigurationAppServiceImpl implements ConfigurationAppService {
    private final ConfigurationRepository configurationRepository;

    public ConfigurationAppServiceImpl(final SGEDBHelper sgeDBHelper) {
        if (sgeDBHelper == null)
            throw new IllegalArgumentException();

        this.configurationRepository = new ConfigurationRepositoryImpl(sgeDBHelper);
    }

    @Override
    public Configuracion findFirst() {
        return this.configurationRepository.getFirst();
    }

    @Override
    public void save(Configuracion configuration) {
        this.configurationRepository.store(configuration);
    }
}
