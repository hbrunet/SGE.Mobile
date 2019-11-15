package com.sge.mobile.presentation;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
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
    private ListView lvOrders;
    private TextView lblTableName;
    private static final int PICK_TABLE_REQUEST = 2;
    private int tableNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_orders);
        this.setTitle(this.getString(R.string.title_activity_table_orders)
                + " - " + UserSession.getInstance().getUserName());
        this.lvOrders = findViewById(R.id.lvOrders);
        this.lblOrdersCount = findViewById(R.id.lblOrdersCount);
        this.lblTableName = findViewById(R.id.lblTableName);
    }

    public void selectTable(View button) {
        Intent intent = new Intent(this, TablesActivity.class);
        startActivityForResult(intent, PICK_TABLE_REQUEST);
    }

    public void searchOrders(View button) {
        GetTableStatusAsyncTask getTableStatusAsyncTask = new GetTableStatusAsyncTask(TableOrdersActivity.this, tableNumber);
        getTableStatusAsyncTask.execute();
    }

    public void polulateOrders(final ResumenMesa resumenMesa) {
        try {
            this.lvOrders.setAdapter(new GenericAdapter(this, R.layout.order_row,
                    new ArrayList<>(resumenMesa.getDetalle())) {
                @Override
                public void onItem(Object item, final View view) {
                    if (item != null) {
                        ResumenMesaDetalle resumenMesaDetalle = (ResumenMesaDetalle) item;
                        TextView lblProduct = view.findViewById(R.id.lblProd);
                        TextView lblQuantity = view.findViewById(R.id.lblQuantity);
                        TextView lblAccessories = view.findViewById(R.id.lblAcces);
                        TextView lblDate = view.findViewById(R.id.lblDate);
                        TextView lblOrderId = view.findViewById(R.id.lblOrderId);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_TABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    tableNumber = data.getIntExtra("tableId", -1);
                    this.lblTableName.setText(String.format("%s - %s", data.getStringExtra("sectorName"), data.getStringExtra("tableName")));
                }
                else {
                    tableNumber = -1;
                    this.lblTableName.setText("SIN MESA");
                }
            }
        }
    }
}
