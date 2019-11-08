package com.sge.mobile.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.sge.mobile.domain.model.LineaPedido;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.Rubro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TablesActivity extends AppCompatActivity {
    private ExpandableListView tablesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        this.setTitle("Mesas");
        this.tablesList = (ExpandableListView) findViewById(R.id.tablesList);
        this.populateTables();
    }

    public void populateTables() {
        try {
            String NAME = "NAME";

            String groupFrom[] = {NAME};
            int groupTo[] = {0}; // {R.id.heading};
            String childFrom[] = {NAME};
            int childTo[] = {0}; // {R.id.childItem};

            List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
            List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
            final String groupItems[] = {"Animals", "Birds"};
            final String[][] childItems = {{"Dog", "Cat", "Tiger"}, {"Crow", "Sparrow"}};

            for (int i = 0; i < groupItems.length; i++) {
                Map<String, String> curGroupMap = new HashMap<String, String>();
                groupData.add(curGroupMap);
                curGroupMap.put(NAME, groupItems[i]);

                List<Map<String, String>> children = new ArrayList<Map<String, String>>();
                for (int j = 0; j < childItems[i].length; j++) {
                    Map<String, String> curChildMap = new HashMap<String, String>();
                    children.add(curChildMap);
                    curChildMap.put(NAME, childItems[i][j]);
                }
                childData.add(children);
            }

            final SimpleExpandableListAdapter expListAdapter = new SimpleExpandableListAdapter(this, groupData,
                    0,
                    groupFrom, groupTo,
                    childData, 0,
                    childFrom, childTo);

            // perform set on group click listener event
            tablesList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                    // display a toast with group name whenever a user clicks on a group item
                    Toast.makeText(getApplicationContext(), "Group Name Is :" + groupItems[groupPosition], Toast.LENGTH_LONG).show();

                    return false;
                }
            });
            // perform set on child click listener event
            tablesList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    // display a toast with child name whenever a user clicks on a child item
                    Toast.makeText(getApplicationContext(), "Child Name Is :" + childItems[groupPosition][childPosition], Toast.LENGTH_LONG).show();
                    return false;
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
