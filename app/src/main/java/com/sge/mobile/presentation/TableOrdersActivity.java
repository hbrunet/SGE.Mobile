package com.sge.mobile.presentation;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sge.mobile.domain.model.Mesa;
import com.sge.mobile.domain.model.ResumenMesa;
import com.sge.mobile.domain.model.ResumenMesaDetalle;

import java.util.ArrayList;

/**
 * Created by Daniel on 17/04/14.
 */
public class TableOrdersActivity extends AppCompatActivity {
    public TextView lblOrdersCount;
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
        this.lblOrdersCount = (TextView) findViewById(R.id.lblOrdersCount);
        this.populateTables();
    }

    public void searchOrders(View button) {
        GetTableStatusAsyncTask getTableStatusAsyncTask = new GetTableStatusAsyncTask(TableOrdersActivity.this, tableNumber);
        getTableStatusAsyncTask.execute();
    }

    public void populateTables() {

        ArrayAdapter<Mesa> adapter = new ArrayAdapter<Mesa>(this,
                android.R.layout.simple_spinner_dropdown_item,
                UserSession.getInstance().getTables());

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTables.setAdapter(adapter);

        this.spinnerTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mesa selectedTable = (Mesa)spinnerTables.getSelectedItem();
                tableNumber = selectedTable.getId();
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
                        TextView lblOrderId = (TextView) view.findViewById(R.id.lblOrderId);

                        if (lblOrderId != null) {
                            lblOrderId.setText(Integer.toString(resumenMesaDetalle.getPedidoId()));
                        }

                        if (lblProduct != null) {
                            if (resumenMesaDetalle.isAnulado()) {
                                lblProduct.setPaintFlags(lblProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            } else {
                                lblProduct.setPaintFlags(lblProduct.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                            if (resumenMesaDetalle.isDescargado()) {
                                lblProduct.setTextColor(Color.parseColor("#FDD835"));
                            } else {
                                lblProduct.setTextColor(getTextColorOfTheme(android.R.attr.textColorPrimary));
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
                            if (resumenMesaDetalle.isDescargado()) {
                                lblAccessories.setTextColor(Color.parseColor("#F9A825"));
                            } else {
                                lblAccessories.setTextColor(getTextColorOfTheme(android.R.attr.textColorSecondary));
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

    private int getTextColorOfTheme(int colorCode) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(colorCode, typedValue, true);
        TypedArray arr =
                this.obtainStyledAttributes(typedValue.data, new int[]{
                        colorCode});
        int color = arr.getColor(0, -1);
        arr.recycle();
        return color;
    }
}
