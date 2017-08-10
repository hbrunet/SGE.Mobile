package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sge.mobile.domain.model.ResumenMesa;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by DEVNET on 26/07/2017.
 */

public class GetTableStatusAsyncTask extends AsyncTask<Void, Void, ResumenMesa> {

    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private final int table;
    private boolean isConnectedToSyncService;

    public GetTableStatusAsyncTask(Context activity, int table) {
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
    protected ResumenMesa doInBackground(Void... params) {
        ResumenMesa resumenMesa = null;

        try {
            int i = 0;
            do {
                this.isConnectedToSyncService = this.sgeOrderServiceAgent.testConnection(UserSession.getInstance().getSgeServiceUrl());
                if (isConnectedToSyncService) {
                    resumenMesa = this.sgeOrderServiceAgent.getTableStatus(UserSession.getInstance().getUserId(),
                            table,
                            UserSession.getInstance().getSgeServiceUrl());
                }
                i++;
            } while (!this.isConnectedToSyncService && i <= 10);
        } catch (Exception ex) {
        }
        return resumenMesa;
    }

    @Override
    protected void onPostExecute(ResumenMesa resumenMesa) {
        progressDialog.dismiss();
        if (resumenMesa != null) {
            if (!resumenMesa.isValida()) {
                showToast(resumenMesa.getError());
            }
            ((TableOrdersActivity) activity).polulateOrders(resumenMesa);
            ((TableOrdersActivity) activity).lblOrdersCount.setText(String.format("Cantidad de Pedidos: %d",
                    resumenMesa.getCantidadPedidos()));
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
