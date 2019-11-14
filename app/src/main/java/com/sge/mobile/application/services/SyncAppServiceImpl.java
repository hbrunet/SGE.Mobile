package com.sge.mobile.application.services;

import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;
import com.sge.mobile.presentation.UserSession;

import java.util.List;

/**
 * Created by hbrunet on 12/07/2017.
 */

public class SyncAppServiceImpl implements SyncAppService {

    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final CategoryAppService categoryAppService;
    private final ProductAppService productAppService;
    private final SGEDBHelper sgeDBHelper;

    public SyncAppServiceImpl(final SGEDBHelper sgeDBHelper) {
        if (sgeDBHelper == null)
            throw new IllegalArgumentException();
        this.sgeDBHelper = sgeDBHelper;
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.categoryAppService = new CategoryAppServiceImpl(sgeDBHelper);
        this.productAppService = new ProductAppServiceImpl(sgeDBHelper);
    }

    @Override
    public void syncData() {
        this.productAppService.removeAll();
        this.categoryAppService.removeAll();

        List<Rubro> newCategories = this.sgeOrderServiceAgent.synchcronizeCategories(UserSession.getInstance().getSgeServiceUrl());
        for (Rubro category : newCategories) {
            this.categoryAppService.save(category);
        }

        List<Producto> newProducts = this.sgeOrderServiceAgent.synchronizeProducts(this.categoryAppService, UserSession.getInstance().getSgeServiceUrl());
        for (Producto product : newProducts) {
            this.productAppService.save(product);
        }

        List<Accesorio> newAccessories = this.sgeOrderServiceAgent.synchronizeAccessories(this.productAppService, UserSession.getInstance().getSgeServiceUrl());
        for (Accesorio accessorie : newAccessories) {
            this.productAppService.saveAccessorie(accessorie);
        }

        UserSession.getInstance().setTablesNumber(this.sgeOrderServiceAgent.getTables(UserSession.getInstance().getSgeServiceUrl()));

        UserSession.getInstance().setTables(this.sgeOrderServiceAgent.getTablesArray(UserSession.getInstance().getSgeServiceUrl()));

        UserSession.getInstance().setSectors(this.sgeOrderServiceAgent.getSectorsForWaiter(UserSession.getInstance().getUserId(),
                                                                                            UserSession.getInstance().getSgeServiceUrl()));
    }
}
