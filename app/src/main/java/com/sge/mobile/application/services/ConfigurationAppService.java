package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Configuracion;

/**
 * Created by Daniel on 10/04/14.
 */
public interface ConfigurationAppService {
    Configuracion findFirst();

    void save(Configuracion configuration);
}
