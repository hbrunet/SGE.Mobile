package com.sge.mobile.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sge.mobile.domain.model.LineaPedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 17/04/14.
 */
public class TableOrdersActivity extends AppCompatActivity {
    private Spinner spinnerTables;
    private ListView lvOrders;


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
        polulateOrders();
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
                int tableNumber = Integer.parseInt(selectedTable.substring(selectedTable.indexOf("-") + 2));
                UserSession.getInstance().getOrder().setNroMesa(tableNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void polulateOrders() {
        try {
            this.lvOrders.setAdapter(new GenericAdapter(this, R.layout.order_row,
                    new ArrayList<LineaPedido>(UserSession.getInstance().getOrder().getLineasPedido())) {
                @Override
                public void onItem(Object item, final View view) {
                    final LineaPedido orderLine = (LineaPedido) item;
                    if (item != null) {
                        TextView lblProduct = (TextView) view.findViewById(R.id.lblProduct);
                        TextView lblQuantity = (TextView) view.findViewById(R.id.lblQuantity);
                        if (lblProduct != null)
                            lblProduct.setText(orderLine.getDescripcionProducto());
                        if (lblQuantity != null) {
                            lblQuantity.setText("[" + 1 + "]");
                        }
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
