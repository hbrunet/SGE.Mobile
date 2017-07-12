package com.sge.mobile.domain.model;

import com.sge.mobile.domain.core.Repository;

/**
 * Created by Daniel on 10/04/14.
 */
public interface ConfigurationRepository extends Repository<Configuracion> {
    Configuracion getFirst();
}
