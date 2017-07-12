package com.sge.mobile.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Producto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Daniel on 05/04/14.
 */
public class AccessoriesDialog extends DialogFragment {

    private ListView lvAccessories;
    private Producto product;
    private ProductsActivity productsActivity;
    private List<Integer> selectedAccessories;
    private String[] accessoriesNames;

    public AccessoriesDialog(Producto product, ProductsActivity productsActivity) {
        this.product = product;
        this.productsActivity = productsActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.selectedAccessories = new ArrayList<Integer>();
        Collection<Accesorio> accessories = this.product.getAccesorios();
        this.accessoriesNames = new String[accessories.size()];
        boolean[] chequedAccessories = new boolean[accessories.size()];
        int i = 0;
        for (Accesorio accessory : accessories) {
            this.accessoriesNames[i] = accessory.getProducto().getDescripcion();
            chequedAccessories[i] = accessory.isPorDefecto();
            if (chequedAccessories[i]) {
                this.selectedAccessories.add(i);
            }
            i++;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Accesorios")
                .setMultiChoiceItems(this.accessoriesNames, chequedAccessories,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    selectedAccessories.add(which);
                                } else if (selectedAccessories.contains(which)) {
                                    selectedAccessories.remove(Integer.valueOf(which));
                                }
                            }
                        }
                )

                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LineaPedido orderLine = new LineaPedido();
                        orderLine.setProductoId(product.getId());
                        orderLine.setDescripcionProducto(product.getDescripcion());
                        float price = product.getPrecio();
                        if (selectedAccessories.size() > 0) {
                            String accessoriesDescription = "C/ ";
                            for (Integer item : selectedAccessories) {
                                String accessoryName = accessoriesNames[item];
                                Producto accessory = productsActivity.getProductAppService().findProductByDescription(accessoryName);
                                orderLine.getAccesorios().add(accessory.getId());
                                accessoriesDescription += accessory.getDescripcion() + ", ";
                                price = price + accessory.getPrecio();
                            }
                            accessoriesDescription = accessoriesDescription.substring(0, accessoriesDescription.length() - 2);
                            orderLine.setDescripcionAccesorios(accessoriesDescription);
                        }
                        orderLine.setPrecio(price);
                        UserSession.getInstance().getOrder().getLineasPedido().add(orderLine);
                        Toast.makeText(productsActivity, product.getDescripcion() + ", " + "fue agregado al pedido.", Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_product_accessories, container);
        this.lvAccessories = (ListView) view.findViewById(R.id.lvAccessories);
        getDialog().setTitle("Accesorios");
        this.polulateAccessories();
        return view;
    }*/

    private void polulateAccessories() {
        try {
            Collection<Accesorio> accessories = this.product.getAccesorios();
            this.lvAccessories.setAdapter(new GenericAdapter(productsActivity, R.layout.accessory_row, new ArrayList<Accesorio>(accessories)) {
                @Override
                public void onItem(Object item, View view) {
                    Accesorio accessory = (Accesorio) item;
                    if (item != null) {
                        TextView lblAccessory = (TextView) view.findViewById(R.id.lblAccessory);
                        if (lblAccessory != null)
                            lblAccessory.setText(accessory.getProducto().getDescripcion());
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
