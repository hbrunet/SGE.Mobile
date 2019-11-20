package com.sge.mobile.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.application.services.ConfigurationAppServiceImpl;
import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.application.services.ProductAppServiceImpl;
import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.infrastructure.data.SGEDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ProductsActivity extends AppCompatActivity {
    private SGEDBHelper sgeDBHelper;
    private ExpandableListView productsList;
    private ProductAppService productAppService;
    private ConfigurationAppService configurationAppService;
    private boolean doubleBackToExitPressedOnce;
    private EditText searchText;

    public ProductAppService getProductAppService() {
        if (productAppService == null) {
            productAppService = new ProductAppServiceImpl(this.getSgeDBHelper());
        }
        return productAppService;
    }

    public ConfigurationAppService getConfigurationAppService() {
        if (configurationAppService == null) {
            configurationAppService = new ConfigurationAppServiceImpl(this.getSgeDBHelper());
        }
        return configurationAppService;
    }

    private SGEDBHelper getSgeDBHelper() {
        if (sgeDBHelper == null) {
            sgeDBHelper = OpenHelperManager.getHelper(this, SGEDBHelper.class);
        }
        return sgeDBHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sgeDBHelper != null) {
            OpenHelperManager.releaseHelper();
            sgeDBHelper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        this.setTitle(this.getString(R.string.title_activity_products)
                + " - " + UserSession.getInstance().getUserName());
        this.productsList = findViewById(R.id.productsList);
        this.searchText = findViewById(R.id.searchText);
        this.searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                populateProducts();
            }
        });

        this.populateProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.btnConfig);
        if (UserSession.getInstance().isAdministrator()) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.btnSynchronize) {
            SynchronizeAsyncTask synchronizeAsyncTask = new SynchronizeAsyncTask(ProductsActivity.this);
            synchronizeAsyncTask.execute();
            return true;
        } else if (id == R.id.btnLogOut) {
            LogOutAsyncTask logOutAsyncTask = new LogOutAsyncTask(ProductsActivity.this);
            logOutAsyncTask.execute();
            return true;
        } else if (id == R.id.btnConfig) {
            ConfigurationDialog configurationDialog = new ConfigurationDialog();
            configurationDialog.setProductsActivity(this);
            configurationDialog.setConfigurationAppService(this.getConfigurationAppService());
            configurationDialog.show(getSupportFragmentManager(), "ConfigurationDialog");
            return true;
        } else if (id == R.id.btnChangePassword) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.btnViewOrdersTable) {
            Intent intent = new Intent(this, TableOrdersActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulse de nuevo para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void populateProducts() {
        try {
            List<Producto> products = getProductAppService().FindProducts((searchText.getText() != null) ? searchText.getText().toString() : "");

            Map<String, List<Producto>> productCollections = new TreeMap<>();
            for (Producto product : products) {
                String key = product.getRubro().getDescripcion();
                if (productCollections.containsKey(key)) {
                    List<Producto> list = productCollections.get(key);
                    list.add(product);

                } else {
                    List<Producto> list = new ArrayList<>();
                    list.add(product);
                    productCollections.put(key, list);
                }
            }

            final ExpandableProductListAdapter expListAdapter = new ExpandableProductListAdapter(this, productCollections);
            this.productsList.setAdapter(expListAdapter);

            this.productsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    Producto selectedProduct = (Producto) expListAdapter.getChild(
                            groupPosition, childPosition);

                    if (selectedProduct.getAccesorios() == null || selectedProduct.getAccesorios().size() == 0) {
                        LineaPedido orderLine = new LineaPedido();
                        orderLine.setProductoId(selectedProduct.getId());
                        orderLine.setDescripcionProducto(selectedProduct.getDescripcion());
                        orderLine.setAccesorios(selectedProduct.getIdsAccesoriosPorDefecto());
                        orderLine.setDescripcionAccesorios(selectedProduct.getDescripcionAccesoriosPorDefecto());
                        orderLine.setPrecio(selectedProduct.getPrecio());
                        UserSession.getInstance().getOrder().getLineasPedido().add(orderLine);
                        Toast.makeText(getBaseContext(), selectedProduct.getDescripcion() + ", " + "fue agregado al pedido.", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        showAccessoriesDialog(selectedProduct);
                    }

                    return true;
                }
            });

            if (searchText.getText().length() > 0) {
                for (int i = 0; i < expListAdapter.getGroupCount(); i++) {
                    productsList.expandGroup(i);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showAccessoriesDialog(Producto product) {
        ProductAccessoriesDialog productAccessoriesDialog = new ProductAccessoriesDialog();
        productAccessoriesDialog.setProduct(product);
        productAccessoriesDialog.setProductsActivity(this);
        productAccessoriesDialog.populateSelectedAccessories();
        productAccessoriesDialog.show(getSupportFragmentManager(), "ProductAccessoriesDialog");
    }

    public void viewOrder(View button) {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }
}
