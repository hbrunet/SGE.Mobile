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
    private Map<String, List<Producto>> productCollections;

    public ExpandableProductListAdapter(Activity context, Map<String, List<Producto>> productCollections) {
        this.context = context;
        this.productCollections = productCollections;
    }

    @Override
    public int getGroupCount() {
        return  this.productCollections.keySet().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.productCollections.get(this.productCollections.keySet().toArray()[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.productCollections.keySet().toArray()[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.productCollections.get(this.productCollections.keySet().toArray()[groupPosition]).get(childPosition);
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
        TextView item = convertView.findViewById(R.id.txtCategoryName);
        item.setText(categoryName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Producto product = (Producto) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_row, null);
        }

        ImageView imgAccesdories = convertView.findViewById(R.id.imgAccessories);
        if (product.getAccesorios() == null || product.getAccesorios().size() == 0) {
            imgAccesdories.setVisibility(View.INVISIBLE);
        } else {
            imgAccesdories.setVisibility(View.VISIBLE);
        }

        TextView txtProductName = convertView.findViewById(R.id.txtProductName);
        TextView txtProductPrice = convertView.findViewById(R.id.txtProductPrice);
        txtProductName.setText(product.getDescripcion());
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
