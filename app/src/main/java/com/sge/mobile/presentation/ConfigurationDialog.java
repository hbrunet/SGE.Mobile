package com.sge.mobile.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.domain.core.DomainObjectUtils;
import com.sge.mobile.domain.model.Configuracion;

/**
 * Created by Daniel on 10/04/14.
 */
public class ConfigurationDialog extends DialogFragment {
    private ProductsActivity productsActivity;
    private ConfigurationAppService configurationAppService;

    public ConfigurationDialog() { super(); }

    public void setProductsActivity(ProductsActivity productsActivity) {
        this.productsActivity = productsActivity;
    }

    public void setConfigurationAppService(ConfigurationAppService configurationAppService) {
        this.configurationAppService = configurationAppService;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Configuración");
        builder.setIcon(android.R.drawable.ic_menu_preferences);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_configuration, null);
        final TextView txtServiceUrl = (TextView) view.findViewById(R.id.txtServiceUrl);
        Configuracion config = this.configurationAppService.findFirst();
        txtServiceUrl.setText(config.getUrlServicioWebSge());

        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String serviceUrlText = txtServiceUrl.getText().toString().trim();

                        if (DomainObjectUtils.textHasContent(serviceUrlText)) {
                            Configuracion config = configurationAppService.findFirst();
                            config.setUrlServicioWebSge(serviceUrlText);
                            configurationAppService.save(config);
                            UserSession.getInstance().setSgeServiceUrl(serviceUrlText);
                        } else {
                            Toast.makeText(productsActivity, "El campo URL Servicio Web es obligatorio.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
