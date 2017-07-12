package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by Daniel on 06/04/14.
 */
public class SendOrderAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private boolean connected;

    public SendOrderAsyncTask(Context activity) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Enviando Pedido");
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
        try {
            int i = 0;
            do {
                this.connected = this.sgeOrderServiceAgent.connected(UserSession.getInstance().getSgeServiceUrl());
                if (connected) {
                    this.sgeOrderServiceAgent.sendOrder(UserSession.getInstance().getUserId(),
                            UserSession.getInstance().getOrder().getNroMesa(),
                            UserSession.getInstance().getOrder().toString(),
                            UserSession.getInstance().getOrder().getObservacion(),
                            UserSession.getInstance().getSgeServiceUrl());
                }
                i++;
            } while (!this.connected && i <= 10);
        } catch (Exception e) {
        }
        return this.connected;
    }

    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        if (result) {
            showToast("El pedido fue enviado correctamente.");
            UserSession.getInstance().setOrder(null);
            ((OrderActivity) activity).finish();
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
