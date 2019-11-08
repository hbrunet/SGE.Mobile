package com.sge.mobile.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.application.services.ConfigurationAppServiceImpl;
import com.sge.mobile.domain.core.DomainObjectUtils;
import com.sge.mobile.domain.model.Configuracion;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.infrastructure.data.SGEDBHelper;
import com.sge.mobile.infrastructure.data.serviceagents.SGEOrderServiceAgentImpl;

/**
 * Created by Daniel on 20/08/13.
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final ProgressDialog progressDialog;
    private final SGEOrderServiceAgent sgeOrderServiceAgent;
    private final ConfigurationAppService configurationAppService;
    private final Context activity;
    private String user;
    private String password;
    private boolean connected;

    public LoginAsyncTask(Context activity, String user, String password) {
        this.sgeOrderServiceAgent = new SGEOrderServiceAgentImpl();
        this.user = user;
        this.password = password;
        this.activity = activity;
        this.configurationAppService = new ConfigurationAppServiceImpl(OpenHelperManager.getHelper(activity, SGEDBHelper.class));
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Autenticando");
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
    protected Boolean doInBackground(Void... voids) {
        boolean result = false;
        if (!user.equals("admin")) {
            try {
                Configuracion config = this.configurationAppService.findFirst();

                if (config != null && DomainObjectUtils.textHasContent(config.getUrlServicioWebSge())) {
                    int i = 0;
                    do {
                        this.connected = this.sgeOrderServiceAgent.testConnection(config.getUrlServicioWebSge());
                        if (this.connected) {
                            UserSession.getInstance().setSgeServiceUrl(config.getUrlServicioWebSge());
                            UserSession.getInstance()
                                    .setUserId(this.sgeOrderServiceAgent.logIn(user, password, UserSession.getInstance()
                                            .getSgeServiceUrl()));
                            UserSession.getInstance()
                                    .setTablesNumber(this.sgeOrderServiceAgent.getTables(UserSession.getInstance()
                                            .getSgeServiceUrl()));

                            UserSession.getInstance().setTables(this.sgeOrderServiceAgent.getTablesArray(UserSession.getInstance().getSgeServiceUrl()));
                            UserSession.getInstance().setSectors(this.sgeOrderServiceAgent.getSectorsArray(UserSession.getInstance().getSgeServiceUrl()));
                        }
                        i++;
                    } while (!this.connected && i <= 10);
                    result = this.connected;
                }
            } catch (Exception e) {
            }
        } else {
            result = true;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        if (!user.equals("admin")) {
            if (result) {
                if (UserSession.getInstance().getUserId() > 0) {
                    UserSession.getInstance().setAdministrator(false);
                    UserSession.getInstance().setUserName(user);
                    UserSession.getInstance().setPassword(password);
                    Intent intent = new Intent(activity, ProductsActivity.class);
                    activity.startActivity(intent);
                    ((LoginActivity) activity).finish();
                } else {
                    showToast("El nombre de usuario o la contraseña especificados son incorrectos.");
                }
            } else {
                showToast("Error: No fue posible establecer conexión con el servicio SGE. " +
                        "\nAsegúrese de tener habilitada la conexion Wi-Fi y vuela a intentarlo.");
            }
        } else {
            if (password.equals(this.configurationAppService.findFirst().getClaveAdministrador())) {
                UserSession.getInstance().setAdministrator(true);
                UserSession.getInstance().setUserName(user);
                UserSession.getInstance().setPassword(password);
                Intent intent = new Intent(activity, ProductsActivity.class);
                activity.startActivity(intent);
                ((LoginActivity) activity).finish();
            } else {
                showToast("El nombre de usuario o la contraseña especificados son incorrectos.");
            }
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT);
        error.show();
    }
}
