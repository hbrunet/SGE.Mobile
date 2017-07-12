package com.sge.mobile.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Producto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 05/04/14.
 */
public class ProductAccessoriesDialog extends DialogFragment {

    private ListView lvAccessories;
    private Producto product;
    private ProductsActivity productsActivity;
    private List<Accesorio> selectedAccessories;

    public ProductAccessoriesDialog(Producto product, ProductsActivity productsActivity) {
        this.product = product;
        this.productsActivity = productsActivity;
        this.selectedAccessories = new ArrayList<Accesorio>();
        for (Accesorio item : this.product.getAccesorios()) {
            if (item.isPorDefecto())
                this.selectedAccessories.add(item);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Accesorios " + this.product.getDescripcion());
        builder.setIcon(android.R.drawable.ic_dialog_dialer);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_product_accessories, null);
        this.lvAccessories = (ListView) view.findViewById(R.id.lvAccessories);
        this.polulateAccessories();
        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LineaPedido orderLine = new LineaPedido();
                        orderLine.setProductoId(product.getId());
                        orderLine.setDescripcionProducto(product.getDescripcion());
                        float price = product.getPrecio();

                        if (selectedAccessories.size() > 0) {
                            List<Accesorio> uniqueAccessories = new ArrayList<Accesorio>();
                            for (Accesorio item : selectedAccessories) {
                                orderLine.getAccesorios().add(item.getProducto().getId());
                                if (!uniqueAccessories.contains(item)) {
                                    uniqueAccessories.add(item);
                                }
                                price = price + item.getProducto().getPrecio();
                            }

                            String accessoriesDescription = "C/ ";
                            for (Accesorio item : uniqueAccessories) {
                                int occurrences = Collections.frequency(selectedAccessories, item);
                                accessoriesDescription += (occurrences > 1 ? (Integer.toString(occurrences) + " ") : "")
                                        + item.getProducto().getDescripcion() + ", ";
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
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void polulateAccessories() {
        try {
            Collection<Accesorio> accessories = this.product.getAccesorios();
            this.lvAccessories.setAdapter(new GenericAdapter(productsActivity, R.layout.accessory_row, new ArrayList<Accesorio>(accessories)) {
                @Override
                public void onItem(Object item, View view) {
                    final Accesorio accessory = (Accesorio) item;
                    if (item != null) {
                        TextView lblAccessory = (TextView) view.findViewById(R.id.lblAccessory);
                        if (lblAccessory != null)
                            lblAccessory.setText(accessory.getProducto().getDescripcion());
                        final TextView lblAmount = (TextView) view.findViewById(R.id.lblAmount);
                        if (lblAmount != null) {
                            int occurrences = Collections.frequency(selectedAccessories, accessory);
                            lblAmount.setText(Integer.toString(occurrences));
                        }
                        ImageView btnDown = (ImageView) view.findViewById(R.id.btnDown);
                        if (btnDown != null) {
                            btnDown.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedAccessories.remove(accessory);
                                    int occurrences = Collections.frequency(selectedAccessories, accessory);
                                    lblAmount.setText(Integer.toString(occurrences));
                                }
                            });
                        }
                    }
                }
            });
            this.lvAccessories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Accesorio a = (Accesorio) lvAccessories.getAdapter().getItem(position);
                    selectedAccessories.add(a);
                    int occurrences = Collections.frequency(selectedAccessories, a);
                    TextView lblAmount = (TextView) view.findViewById(R.id.lblAmount);
                    lblAmount.setText(Integer.toString(occurrences));
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
