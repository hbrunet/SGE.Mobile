package com.sge.mobile.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.domain.model.LineaPedido;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 03/04/14.
 */
public class ExpandableOrderListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<LineaPedido>> orderLineCollections;
    private List<String> orders;
    private ProductAppService productAppService;

    public ExpandableOrderListAdapter(Activity context, List<String> orders,
                                      Map<String, List<LineaPedido>> orderLineCollections,
                                      ProductAppService productAppService) {
        this.context = context;
        this.orderLineCollections = orderLineCollections;
        this.orders = orders;
        this.productAppService = productAppService;
    }

    @Override
    public int getGroupCount() {
        return this.orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.orderLineCollections.get(this.orders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.orders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.orderLineCollections.get(this.orders.get(groupPosition)).get(childPosition);
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
        String orderName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.order_row, null);
        }
        TextView txtOrderDescription = (TextView) convertView.findViewById(R.id.txtOrderDescription);
        txtOrderDescription.setText(orderName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final LineaPedido orderLine = (LineaPedido) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_line_row2, null);
        }

        TextView lblProduct = (TextView) convertView.findViewById(R.id.lblProduct);
        TextView lblAccessories = (TextView) convertView.findViewById(R.id.lblAccessories);
        lblProduct.setText(orderLine.getDescripcionProducto());
        lblAccessories.setText(orderLine.getDescripcionAccesorios());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
