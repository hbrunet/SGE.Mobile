package com.sge.mobile.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sge.mobile.application.services.CategoryAppService;
import com.sge.mobile.application.services.CategoryAppServiceImpl;
import com.sge.mobile.application.services.ConfigurationAppService;
import com.sge.mobile.application.services.ConfigurationAppServiceImpl;
import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.application.services.ProductAppServiceImpl;
import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.infrastructure.data.SGEDBHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ProductsActivity extends AppCompatActivity {
    private SGEDBHelper sgeDBHelper;
    private ExpandableListView productsList;
    private CategoryAppService categoryAppService;
    private ProductAppService productAppService;
    private ConfigurationAppService configurationAppService;
    private boolean doubleBackToExitPressedOnce;

    private CategoryAppService getCategoryAppService() {
        if (categoryAppService == null) {
            categoryAppService = new CategoryAppServiceImpl(this.getSgeDBHelper());
        }
        return categoryAppService;
    }

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
        this.productsList = (ExpandableListView) findViewById(R.id.productsList);
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

    private List<String> getCategoriesDescriptions() {
        List<Rubro> categories = this.getCategoryAppService().findActiveCategories();
        List<String> categoriesDescriptions = new ArrayList<String>();
        for (Rubro item : categories) {
            categoriesDescriptions.add(item.getDescripcion());
        }
        return categoriesDescriptions;
    }

    private Map<String, List<String>> getProductsCollections() {
        List<Rubro> categories = this.getCategoryAppService().findCategories();
        Map<String, List<String>> productCollections = new LinkedHashMap<String, List<String>>();
        for (Rubro category : categories) {
            List<String> productsNames = new ArrayList<String>();
            for (Producto prod : category.getProductos()) {
                if (!prod.isAccesorio() && prod.isVisible() && prod.getEstado() == 1) {
                    productsNames.add(prod.getDescripcion());
                }
            }
            productCollections.put(category.getDescripcion(), productsNames);
        }
        return productCollections;
    }

    public void populateProducts() {
        try {
            final ExpandableProductListAdapter expListAdapter = new ExpandableProductListAdapter(
                    this, this.getCategoriesDescriptions(), this.getProductsCollections(),
                    this.getProductAppService());
            this.productsList.setAdapter(expListAdapter);

            this.productsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    final String selectedProduct = (String) expListAdapter.getChild(
                            groupPosition, childPosition);
                    Producto product = productAppService.findProductByDescription(selectedProduct);
                    if (product != null) {
                        if (product.getAccesorios() == null || product.getAccesorios().size() == 0) {
                            LineaPedido orderLine = new LineaPedido();
                            orderLine.setProductoId(product.getId());
                            orderLine.setDescripcionProducto(product.getDescripcion());
                            orderLine.setAccesorios(product.getIdsAccesoriosPorDefecto());
                            orderLine.setDescripcionAccesorios(product.getDescripcionAccesoriosPorDefecto());
                            orderLine.setPrecio(product.getPrecio());
                            UserSession.getInstance().getOrder().getLineasPedido().add(orderLine);
                            Toast.makeText(getBaseContext(), selectedProduct + ", " + "fue agregado al pedido.", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            showAccessoriesDialog(product);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "El producto seleccionado no existe.", Toast.LENGTH_SHORT)
                                .show();
                    }

                    return true;
                }
            });
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
