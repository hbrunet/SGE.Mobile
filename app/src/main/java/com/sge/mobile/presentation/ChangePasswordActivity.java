package com.sge.mobile.presentation;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.application.services.ConfigurationAppServiceImpl;
import com.sge.mobile.domain.core.DomainObjectUtils;
import com.sge.mobile.infrastructure.data.SGEDBHelper;

/**
 * Created by Daniel on 10/04/14.
 */
public class ChangePasswordActivity extends ActionBarActivity {
    private SGEDBHelper sgeDBHelper;
    private ConfigurationAppService configurationAppService;
    private TextView txtOldPassword;
    private TextView txtNewPassword;
    private TextView txtNewPasswordConfirmation;

    private SGEDBHelper getSgeDBHelper() {
        if (sgeDBHelper == null) {
            sgeDBHelper = OpenHelperManager.getHelper(this, SGEDBHelper.class);
        }
        return sgeDBHelper;
    }

    public ConfigurationAppService getConfigurationAppService() {
        if (configurationAppService == null) {
            configurationAppService = new ConfigurationAppServiceImpl(this.getSgeDBHelper());
        }
        return configurationAppService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setTitle(this.getString(R.string.title_activity_change_password)
                + " - " + UserSession.getInstance().getUserName());
        this.txtOldPassword = (TextView) this.findViewById(R.id.txtOldPassword);
        this.txtNewPassword = (TextView) this.findViewById(R.id.txtNewPassword);
        this.txtNewPasswordConfirmation = (TextView) this.findViewById(R.id.txtNewPasswordConfirmation);
    }

    public void ChangePassword(View button) {
        String oldPassword = txtOldPassword.getText().toString().trim();
        String newPassword = txtNewPassword.getText().toString().trim();
        String newPasswordConfirmation = txtNewPasswordConfirmation.getText().toString().trim();

        if (DomainObjectUtils.textHasContent(oldPassword)
                && DomainObjectUtils.textHasContent(newPassword)) {
            if (oldPassword.equals(UserSession.getInstance().getPassword())) {
                if (newPassword.equals(newPasswordConfirmation)) {
                    ChangePasswordAsyncTask changePasswordAsyncTask = new ChangePasswordAsyncTask(ChangePasswordActivity.this
                            , newPassword, this.getConfigurationAppService());
                    changePasswordAsyncTask.execute();
                } else {
                    Toast.makeText(this, "La nueva contraseña y la contraseña de confirmación no coinciden.", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(this, "La contraseña actual no es valida.", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(this, "Los campos Contraseña actual y Nueva contraseña son obligatorios.", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
