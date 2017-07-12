package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.CategoryAppService;
import com.sge.mobile.application.services.CategoryAppServiceImpl;
import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.application.services.ProductAppServiceImpl;
import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

import java.util.List;

/**
 * Created by Daniel on 04/04/14.
 */
public class SynchronizeAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private final CategoryAppService categoryAppService;
    private final ProductAppService productAppService;
    private SGEDBHelper sgeDBHelper;
    private boolean connected;

    public SynchronizeAsyncTask(Context activity) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.categoryAppService = new CategoryAppServiceImpl(this.getSgeDBHelper());
        this.productAppService = new ProductAppServiceImpl(this.getSgeDBHelper());
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Sincronizando");
        progressDialog.setMessage("Espere...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        try {
            progressDialog.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected SGEDBHelper getSgeDBHelper() {
        if (sgeDBHelper == null) {
            sgeDBHelper = OpenHelperManager.getHelper(activity, SGEDBHelper.class);
        }
        return sgeDBHelper;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            int i = 0;
            do {
                this.connected = this.sgeOrderServiceAgent.connected(UserSession.getInstance().getSgeServiceUrl());
                if (connected) {
                    List<Rubro> categories = this.sgeOrderServiceAgent.synchcronizeCategories(UserSession.getInstance().getSgeServiceUrl());
                    for (Rubro category : categories) {
                        this.categoryAppService.save(category);
                    }

                    List<Producto> products = this.sgeOrderServiceAgent.synchronizeProducts(this.categoryAppService, UserSession.getInstance().getSgeServiceUrl());
                    for (Producto product : products) {
                        this.productAppService.save(product);
                    }

                    List<Accesorio> accessories = this.sgeOrderServiceAgent.synchronizeAccessories(this.productAppService, UserSession.getInstance().getSgeServiceUrl());
                    for (Accesorio accessorie : accessories) {
                        this.productAppService.saveAccessorie(accessorie);
                    }

                    UserSession.getInstance().setTablesNumber(this.sgeOrderServiceAgent.getTables(UserSession.getInstance().getSgeServiceUrl()));
                }
                i++;
            } while (!this.connected && i <= 10);
        } catch (Exception e) {
        }
        return this.connected;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        if (result) {
            showToast("La sincronización se realizó correctamente.");
            ((ProductsActivity) activity).populateProducts();
        } else {
            showToast("Error: No fue posible establecer conexión con el servicio SGE. " +
                    "\nAsegúrese de tener habilitada la conexion Wi-Fi y vuela a intentarlo.");
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT);
        error.show();
    }
}
