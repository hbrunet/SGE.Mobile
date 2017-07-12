package com.sge.mobile.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.domain.model.Producto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 03/04/14.
 */
public class ExpandableProductListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> productCollections;
    private List<String> categories;
    private ProductAppService productAppService;

    public ExpandableProductListAdapter(Activity context, List<String> categories,
                                        Map<String, List<String>> productCollections,
                                        ProductAppService productAppService) {
        this.context = context;
        this.productCollections = productCollections;
        this.categories = categories;
        this.productAppService = productAppService;
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.productCollections.get(this.categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.productCollections.get(this.categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String categoryName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.category_row,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.txtCategoryName);
        item.setText(categoryName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String productName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_row, null);
        }

        ImageView imgAccesdories = (ImageView) convertView.findViewById(R.id.imgAccessories);
        Producto product = productAppService.findProductByDescription(productName);
        if (product.getAccesorios() == null || product.getAccesorios().size() == 0) {
            imgAccesdories.setVisibility(View.INVISIBLE);
        } else {
           /* imgAccesdories.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Producto prod = productAppService.findProductByDescription(productName);
                    ((ProductsActivity) context).showAccessoriesDialog(prod);
                }
            });*/
            imgAccesdories.setVisibility(View.VISIBLE);
        }

        TextView txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
        TextView txtProductPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
        txtProductName.setText(productName);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", decimalFormatSymbols);
        txtProductPrice.setText("[$ " + decimalFormat.format(product.getPrecio()) + "]");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
