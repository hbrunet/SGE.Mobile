package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.SyncAppService;
import com.sge.mobile.application.services.SyncAppServiceImpl;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;


/**
 * Created by Daniel on 04/04/14.
 */
public class SynchronizeAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private final SyncAppService syncAppService;
    private SGEDBHelper sgeDBHelper;
    private boolean isConnectedToSyncService;

    public SynchronizeAsyncTask(Context activity) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.syncAppService = new SyncAppServiceImpl(this.getSgeDBHelper());
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
                this.isConnectedToSyncService = this.sgeOrderServiceAgent.testConnection(UserSession.getInstance().getSgeServiceUrl());
                if (isConnectedToSyncService) {
                    this.syncAppService.syncData();
                }
                i++;
            } while (!this.isConnectedToSyncService && i <= 10);
        } catch (Exception e) {
        }
        return this.isConnectedToSyncService;
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
