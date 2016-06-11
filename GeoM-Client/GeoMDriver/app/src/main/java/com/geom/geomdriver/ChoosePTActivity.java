package com.geom.geomdriver;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ChoosePTActivity extends AppCompatActivity {

    public List<PublicTransport> pt_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pt);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //il titolo varia in base alla lista visualizzata
        toolbar.setTitle("Scegli mezzo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Parte il thread per riempire la lista
        *
        * Thread t = new Thread(pt_list);
        * t.start();
        *
        * try{
        *   t.stop();
        * } catch(IOException e){
        *   e.printStackTrace();
        * }
        *
        * */

        pt_list = new ArrayList<>();
        pt_list.add(new PublicTransport(1, "treno", "etr501", "trenitalia", "milano-riccione", true, 12.5, 12.5, "ciao"));
        pt_list.add(new PublicTransport(2, "treno", "etr502", "trenitalia", "milano-riccione", true, 12.5, 12.5, "ciao"));
        pt_list.add(new PublicTransport(3, "treno", "etr503", "trenitalia", "milano-riccione", true, 12.5, 12.5, "ciao"));
        pt_list.add(new PublicTransport(4, "treno", "etr504", "trenitalia", "milano-riccione", true, 12.5, 12.5, "ciao"));
        pt_list.add(new PublicTransport(5, "treno", "etr505", "trenitalia", "milano-riccione", true, 12.5, 12.5, "ciao"));


        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pt_recycler_view);
        //divider
        recyclerView.addItemDecoration(new ListViewDivider(this, ListViewDivider.VERTICAL_LIST));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adapter
        PublicTransportListAdapter publicTransportListAdapter
                = new PublicTransportListAdapter(pt_list);
        recyclerView.setAdapter(publicTransportListAdapter);
    }

}
