package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by Daniel on 06/04/14.
 */
public class LogOutAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private final ProgressDialog progressDialog;
    private boolean connected;

    public LogOutAsyncTask(Context activity) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Cerrando sesión");
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
        if (!UserSession.getInstance().isAdministrator()) {
            try {
                int i = 0;
                do {
                    this.connected = this.sgeOrderServiceAgent.testConnection(UserSession.getInstance().getSgeServiceUrl());
                    if (this.connected) {
                        this.sgeOrderServiceAgent
                                .logOut(UserSession.getInstance().getUserId(), UserSession.getInstance()
                                        .getSgeServiceUrl());
                    }
                    i++;
                } while (!this.connected && i <= 10);
            } catch (Exception e) {
            }
            return this.connected;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        UserSession.getInstance().setOrder(null);
        UserSession.getInstance().setUserId(0);
        UserSession.getInstance().setTablesNumber(0);
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        ((ProductsActivity) activity).finish();
    }
}
