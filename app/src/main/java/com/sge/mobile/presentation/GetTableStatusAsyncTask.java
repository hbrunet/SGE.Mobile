package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sge.mobile.application.services.SyncAppService;
import com.sge.mobile.application.services.SyncAppServiceImpl;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by DEVNET on 26/07/2017.
 */

public class GetTableStatusAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private final int table;
    private boolean isConnectedToSyncService;

    public GetTableStatusAsyncTask(Context activity, int table){
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.activity = activity;
        this.table = table;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Obteniendo estado de mesa");
        progressDialog.setMessage("Espere...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        try {
            progressDialog.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = false;

        try{
            int i = 0;
            do {
                this.isConnectedToSyncService = this.sgeOrderServiceAgent.testConnection(UserSession.getInstance().getSgeServiceUrl());
                if (isConnectedToSyncService) {
                    this.sgeOrderServiceAgent.getTableStatus(UserSession.getInstance().getUserId(),
                            table,
                            UserSession.getInstance().getSgeServiceUrl());
                }
                i++;
            } while (!this.isConnectedToSyncService && i <= 10);
            result = true;
        }catch (Exception ex){
            result = false;
        }

        return result;
    }
}
