package com.sge.mobile.presentation;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sge.mobile.domain.model.ResumenMesa;
import com.sge.mobile.domain.model.ResumenMesaDetalle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 17/04/14.
 */
public class TableOrdersActivity extends AppCompatActivity {
    private Spinner spinnerTables;
    private ListView lvOrders;
    private int tableNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_orders);
        this.setTitle(this.getString(R.string.title_activity_table_orders)
                + " - " + UserSession.getInstance().getUserName());
        this.spinnerTables = (Spinner) findViewById(R.id.spinnerTables);
        this.lvOrders = (ListView) findViewById(R.id.lvOrders);
        this.populateTables();
    }

    public void searchOrders(View button) {
        GetTableStatusAsyncTask getTableStatusAsyncTask = new GetTableStatusAsyncTask(TableOrdersActivity.this, tableNumber);
        getTableStatusAsyncTask.execute();
    }

    public void populateTables() {
        List<String> tablesNames = new ArrayList<String>();

        for (int i = 0; i <= UserSession.getInstance().getTablesNumber(); i++) {
            String tableNumber = String.format("%1$2s", i);
            tableNumber = tableNumber.replace(' ', '0');
            tablesNames.add("Mesa - " + tableNumber);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tablesNames);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTables.setAdapter(dataAdapter);

        String selectedTableNumber = String.format("%1$2s", UserSession.getInstance().getOrder().getNroMesa());
        selectedTableNumber = selectedTableNumber.replace(' ', '0');
        this.spinnerTables.setSelection(dataAdapter.getPosition("Mesa - " + selectedTableNumber));
        this.spinnerTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = spinnerTables.getSelectedItem().toString();
                tableNumber = Integer.parseInt(selectedTable.substring(selectedTable.indexOf("-") + 2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void polulateOrders(final ResumenMesa resumenMesa) {
        try {
            this.lvOrders.setAdapter(new GenericAdapter(this, R.layout.order_row,
                    new ArrayList<>(resumenMesa.getDetalle())) {
                @Override
                public void onItem(Object item, final View view) {
                    if (item != null) {
                        ResumenMesaDetalle resumenMesaDetalle = (ResumenMesaDetalle) item;
                        TextView lblProduct = (TextView) view.findViewById(R.id.lblProd);
                        TextView lblQuantity = (TextView) view.findViewById(R.id.lblQuantity);
                        TextView lblAccessories = (TextView) view.findViewById(R.id.lblAcces);
                        TextView lblDate = (TextView) view.findViewById(R.id.lblDate);

                        if (lblProduct != null) {
                            if (resumenMesaDetalle.isAnulado()) {
                                lblProduct.setPaintFlags(lblProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            } else {
                                lblProduct.setPaintFlags(lblProduct.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                            lblProduct.setText(resumenMesaDetalle.getDescripcion());
                        }

                        if (lblQuantity != null) {
                            lblQuantity.setText(String.format("[ %s ]", resumenMesaDetalle.getCantidad()));
                        }
                        if (lblAccessories != null) {
                            if (resumenMesaDetalle.isAnulado()) {
                                lblAccessories.setPaintFlags(lblAccessories.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            } else {
                                lblAccessories.setPaintFlags(lblAccessories.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                            lblAccessories.setText(resumenMesaDetalle.getAccesorios());
                        }
                        if (lblDate != null) {
                            lblDate.setText(resumenMesaDetalle.getFecha());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
