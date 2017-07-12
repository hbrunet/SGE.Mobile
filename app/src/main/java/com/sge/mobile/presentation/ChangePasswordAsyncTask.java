package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.domain.core.DomainObjectUtils;
import com.sge.mobile.domain.model.Configuracion;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by Daniel on 06/04/14.
 */
public class ChangePasswordAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final Context activity;
    private boolean connected;
    private String newPassword;
    private String errorMessage;
    private ConfigurationAppService configurationAppService;

    public ChangePasswordAsyncTask(Context activity, String newPassword, ConfigurationAppService configurationAppService) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.configurationAppService = configurationAppService;
        this.activity = activity;
        this.newPassword = newPassword;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Cambiando contraseña");
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
        boolean result;
        try {
            if (!UserSession.getInstance().isAdministrator()) {
                int i = 0;
                do {
                    this.connected = this.sgeOrderServiceAgent.connected(UserSession.getInstance().getSgeServiceUrl());
                    if (connected) {
                        // todo: llamar al metodo del servicio para cambiar el password
                    /*this.sgeOrderServiceAgent.sendOrder(UserSession.getInstance().getUserId(),
                            UserSession.getInstance().getOrder().getNroMesa(),
                            UserSession.getInstance().getOrder().toString(),
                            UserSession.getInstance().getOrder().getObservacion(),
                            UserSession.getInstance().getSgeServiceUrl());*/
                    }
                    i++;
                } while (!this.connected && i <= 10);
                result = !DomainObjectUtils.textHasContent(this.errorMessage);
            } else {
                Configuracion config = configurationAppService.findFirst();
                config.setClaveAdministrador(newPassword);
                configurationAppService.save(config);
                result = true;
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            result = false;
        }
        return result;
    }

    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        if (!UserSession.getInstance().isAdministrator()) {
            if (this.connected) {
                if (result) {
                    showToast("La contraseña se ha cambiado.");
                    UserSession.getInstance().setPassword(this.newPassword);
                    ((ChangePasswordActivity) activity).finish();
                } else {
                    showToast(errorMessage);
                }
            } else {
                showToast("Error: No fue posible establecer conexión con el servicio SGE. " +
                        "\nAsegúrese de tener habilitada la conexion Wi-Fi y vuela a intentarlo.");
            }
        } else {
            if (result) {
                showToast("La contraseña se ha cambiado.");
                UserSession.getInstance().setPassword(this.newPassword);
                ((ChangePasswordActivity) activity).finish();
            } else {
                showToast(errorMessage);
            }
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT);
        error.show();
    }
}
