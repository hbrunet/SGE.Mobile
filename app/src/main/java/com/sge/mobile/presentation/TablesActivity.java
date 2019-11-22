package com.sge.mobile.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.sge.mobile.domain.model.Mesa;
import com.sge.mobile.domain.model.Sector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablesActivity extends AppCompatActivity {
    private ExpandableListView tablesList;
    private static final String NAME = "NAME";
    private static final String ID = "ID";
    private SimpleExpandableListAdapter mAdapter;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);
        this.tablesList = findViewById(R.id.tablesList);
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
                populateTables();
            }
        });
        this.populateTables();
    }

    public void populateTables() {
        try {
            boolean addEmpty = getIntent().getBooleanExtra("addEmpty", false);
            List<Map<String, String>> groupData = new ArrayList<>();
            List<List<Map<String, String>>> childData = new ArrayList<>();

            List<Sector> filteredSectors = new ArrayList<>();
            if (searchText.getText().length() > 0) {
                for (Sector sector : UserSession.getInstance().getSectors()) {
                    List<Mesa> filteredTables = new ArrayList<>();
                    for (Mesa table : sector.getMesas()) {
                        if (table.getDescripcion().toUpperCase().contains(searchText.getText().toString().toUpperCase())) {
                            filteredTables.add(table);
                        }
                    }
                    if (filteredTables.size() > 0){
                       Sector s = new Sector(sector, filteredTables);
                       filteredSectors.add(s);
                    }
                }
            } else {
                filteredSectors.addAll(UserSession.getInstance().getSectors());
            }

            Collections.sort(filteredSectors, new Comparator<Sector>() {
                @Override
                public int compare(final Sector s1, final Sector s2) {
                    return  s1.getDescripcion().compareTo(s2.getDescripcion());
                }
            });

            if(addEmpty) {
                Map<String, String> empty = new HashMap<>();
                empty.put(NAME, "SIN MESA");
                empty.put(ID, null);
                groupData.add(empty);
                List<Map<String, String>> children = new ArrayList<>();
                childData.add(children);
            }

            for(Sector sector : filteredSectors){
                Map<String, String> curGroupMap = new HashMap<>();
                curGroupMap.put(NAME, sector.getDescripcion());
                curGroupMap.put(ID, String.valueOf(sector.getId()));
                groupData.add(curGroupMap);

                List<Map<String, String>> children = new ArrayList<>();
                Collections.sort(sector.getMesas(), new Comparator<Mesa>() {
                    @Override
                    public int compare(final Mesa m1, final Mesa m2) {
                        return  m1.getDescripcion().compareTo(m2.getDescripcion());
                    }
                });
                for (Mesa table : sector.getMesas()){
                    Map<String, String> curChildMap = new HashMap<>();
                    curChildMap.put(NAME, table.getDescripcion());
                    curChildMap.put(ID, String.valueOf(table.getId()));
                    children.add(curChildMap);
                }
                childData.add(children);
            }

            String groupFrom[] = {NAME, ID};
            int groupTo[] = {R.id.sectorName, R.id.sectorId};
            String childFrom[] = {NAME, ID};
            int childTo[] = {R.id.tableName, R.id.tableId};

            mAdapter = new SimpleExpandableListAdapter(this,
                                                        groupData,
                                                        R.layout.sector_row,
                                                        groupFrom,
                                                        groupTo,
                                                        childData,
                                                        R.layout.table_row,
                                                        childFrom,
                                                        childTo);
            tablesList.setAdapter(mAdapter);
            tablesList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Map<String, String> selectedGroup = (HashMap<String, String>) mAdapter.getGroup(groupPosition);
                    String sectorId = selectedGroup.get(ID);
                    if (sectorId == null){
                        setResult(RESULT_OK);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
            tablesList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Map<String, String> selectedGroup = (HashMap<String, String>) mAdapter.getGroup(groupPosition);
                    Map<String, String> selectedChild = (HashMap<String, String>) mAdapter.getChild(groupPosition, childPosition);
                    int sectorId = Integer.valueOf(selectedGroup.get(ID));
                    String sectorName = selectedGroup.get(NAME);
                    int tableId = Integer.valueOf(selectedChild.get(ID));
                    String tableName = selectedChild.get(NAME);
                    Intent intent = new Intent();
                    intent.putExtra("tableId", tableId);
                    intent.putExtra("tableName", tableName);
                    intent.putExtra("sectorId", sectorId);
                    intent.putExtra("sectorName", sectorName);
                    Toast.makeText(getBaseContext(), String.format("La mesa %s - %s, fue seleccionada", sectorName, tableName), Toast.LENGTH_SHORT)
                            .show();
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
            });

            if (searchText.getText().length() > 0) {
                for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                    tablesList.expandGroup(i);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
