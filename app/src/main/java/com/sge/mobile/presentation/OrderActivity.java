package com.sge.mobile.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Mesa;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;


public class OrderActivity extends AppCompatActivity {
    private ListView lvOrderLines;
    private TextView txtObservation;
    private TextView lblTotalPrice;
    private TextView lblTableName;
    private static final int PICK_TABLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        this.setTitle(this.getString(R.string.title_activity_order)
                + " - " + UserSession.getInstance().getUserName());
        this.lvOrderLines = findViewById(R.id.lvOrderLines);
        this.txtObservation = findViewById(R.id.txtObservation);
        this.lblTotalPrice = findViewById(R.id.lblTotalPrice);
        this.lblTableName = findViewById(R.id.lblTableName);
        setTableName();
        this.txtObservation.setText(UserSession.getInstance().getOrder().getObservacion());
        this.txtObservation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UserSession.getInstance().getOrder().setObservacion(txtObservation.getText().toString());
            }
        });
        this.polulateOrderLines();
        this.setTotalPrice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void polulateOrderLines() {
        try {
            this.lvOrderLines.setAdapter(new GenericAdapter(this, R.layout.order_line_row,
                    new ArrayList<>(UserSession.getInstance().getOrder().getLineasPedido())) {
                @Override
                public void onItem(Object item, final View view) {
                    final LineaPedido orderLine = (LineaPedido) item;
                    if (item != null) {
                        TextView lblProduct = (TextView) view.findViewById(R.id.lblProduct);
                        TextView lblAccessories = (TextView) view.findViewById(R.id.lblAccessories);
                        //TextView lblPrice = (TextView) view.findViewById(R.id.lblPrice);
                        ImageButton btnRemove = (ImageButton) view.findViewById(R.id.btnRemove);
                        if (lblProduct != null)
                            lblProduct.setText(orderLine.getDescripcionProducto());
                        if (lblAccessories != null)
                            lblAccessories.setText(orderLine.getDescripcionAccesorios());
                        /*if (lblPrice != null) {
                            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                            decimalFormatSymbols.setDecimalSeparator(',');
                            decimalFormatSymbols.setGroupingSeparator('.');
                            DecimalFormat decimalFormat = new DecimalFormat("#.00", decimalFormatSymbols);
                            lblPrice.setText("[$ " + decimalFormat.format(orderLine.getPrecioProducto()) + "]");
                        }*/
                        if (btnRemove != null) {
                            btnRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserSession.getInstance().getOrder().getLineasPedido().remove(orderLine);
                                    polulateOrderLines();
                                    setTotalPrice();
                                }
                            });
                        }
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void sendOrder(View button) {
        if (UserSession.getInstance().getUserId() > 0) {
            if (UserSession.getInstance().getOrder().getLineasPedido().size() > 0) {
                if (UserSession.getInstance().getOrder().getMesa() != null) {
                    SendOrderAsyncTask sendOrderAsyncTask = new SendOrderAsyncTask(OrderActivity.this);
                    sendOrderAsyncTask.execute();
                } else {
                    this.showOrderConfirmationDialog();
                }
            } else {
                Toast.makeText(getBaseContext(), "La lista de productos es obligatoria.", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Inicie sesión.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void selectTable(View button) {
        Intent intent = new Intent(this, TablesActivity.class);
        startActivityForResult(intent, PICK_TABLE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_TABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                setTableName();
            }
        }
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enviar Pedido");
        builder.setIcon(R.drawable.warning);
        builder.setMessage("¿Está seguro que desea enviar el pedido sin mesa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendOrderAsyncTask sendOrderAsyncTask = new SendOrderAsyncTask(OrderActivity.this);
                        sendOrderAsyncTask.execute();
                        dialog.dismiss();
                    }
                }
        );
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setTotalPrice() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
        this.lblTotalPrice.setText("TOTAL A PAGAR: [$ "
                + decimalFormat.format(UserSession.getInstance().getOrder().getPrecioTotal()) + "]");
    }

    private void setTableName() {
        if (UserSession.getInstance().getOrder().getMesa() != null) {
            Mesa mesa = UserSession.getInstance().getOrder().getMesa();
            this.lblTableName.setText(String.format("%s - %s", mesa.getSectorDescripcion(), mesa.getDescripcion()));
        } else {
            this.lblTableName.setText("Sin mesa");
        }
    }
}
