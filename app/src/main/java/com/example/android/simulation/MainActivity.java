package com.example.android.simulation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int gridSideLength = 12;
    private static final int simulationDelay = 500;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView noPeople;
    private TextView noMen;
    private TextView noWomen;
    private TextView time;
    private RecyclerAdapter adapter;

    private Grid grid;
    private boolean play=true;
    private long startTime;
    private int circuitCount = 0;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            time.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 1000);
        }
    };

    private Handler simulationHandler = new Handler();
    private Runnable simulationRunnable = new Runnable() {
        @Override
        public void run() {
            if (circuitCount%2==0){
                grid.makeChildren();
                grid.assassinatePeopleNearBy();
            }else {
                grid.makeMove();
            }
            adapter.setData(grid.convertGridToList());
            noPeople.setText(String.valueOf(grid.countPeople()));
            noMen.setText(String.valueOf(grid.countMen()));
            noWomen.setText(String.valueOf(grid.countWomen()));
            if(grid.isItEnd()){
                timerHandler.removeCallbacks(timerRunnable);
                simulationHandler.removeCallbacks(this);
                fab.setImageResource(R.drawable.ic_play);
                play=true;
            }else {
                circuitCount++;
                simulationHandler.postDelayed(this, simulationDelay);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        fab.setBackgroundResource(R.drawable.ic_play);
        play=true;
        noPeople = findViewById(R.id.noPeople);
        noMen = findViewById(R.id.noMen);
        noWomen = findViewById(R.id.noWomen);
        time = findViewById(R.id.time);

        adapter = new RecyclerAdapter();
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, gridSideLength);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        List<String> emptyGrid = new ArrayList<>();
        for(int i=0; i<gridSideLength*gridSideLength; i++){
            emptyGrid.add(null);
        }
        adapter.setData(emptyGrid);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    fab.setImageResource(R.drawable.ic_pause);
                    play=false;

                    grid = new Grid(gridSideLength);
                    adapter.setData(grid.convertGridToList());
                    noPeople.setText(String.valueOf(grid.countPeople()));
                    noMen.setText(String.valueOf(grid.countMen()));
                    noWomen.setText(String.valueOf(grid.countWomen()));
                    time.setText("00:00");

                    circuitCount=0;
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable,1000);
                    simulationHandler.postDelayed(simulationRunnable, simulationDelay);
                }else {
                    fab.setImageResource(R.drawable.ic_play);
                    play=true;
                    timerHandler.removeCallbacks(timerRunnable);
                    simulationHandler.removeCallbacks(simulationRunnable);
                }
            }
        });
    }
}
