package com.example.shivam97.buku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.shivam97.buku.Buku.mDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HorizontalScrollView horizontalScrollView;
    CardView topRated,forYou,bestAuthors;
    RecyclerView booksRecycler;
    TextView recomm,t;
    BooksAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        horizontalScrollView=findViewById(R.id.hsv);
        forYou= findViewById(R.id.for_you_card);
        topRated=findViewById(R.id.top_rated_card);
        bestAuthors=findViewById(R.id.best_authors_card);
        recomm=findViewById(R.id.recomm_textView);

        OnTouchCard touchCard=new OnTouchCard();
        forYou.setOnTouchListener(touchCard);
        topRated.setOnTouchListener(touchCard);
        bestAuthors.setOnTouchListener(touchCard);
        booksRecycler=findViewById(R.id.images_recycler);
        progressBar=findViewById(R.id.login_progress);
        t=findViewById(R.id.no_network_textview);
        adapter=new BooksAdapter(this,progressBar);
        booksRecycler.setAdapter(adapter);

        booksRecycler.setLayoutManager(new GridLayoutManager(this,2));

        checkConnection();
    }

    private void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            mDatabase= FirebaseDatabase.getInstance().getReference();

            mDatabase.child("ISBN").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    (t).setVisibility(View.GONE);
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        adapter.addISBN(snapshot.getKey(),snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else{
            progressBar.setVisibility(View.GONE);

            (t).setVisibility(View.VISIBLE);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkConnection();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_wishlist)
        {

        }
        else if (id == R.id.nav_mybooks) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class OnTouchCard implements View.OnTouchListener{

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int id=view.getId();

            switch (id){

                case R.id.for_you_card:


                    forYou.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                    recomm.setText("Recommendations for you");
                    topRated.setCardBackgroundColor(null);
                    bestAuthors.setCardBackgroundColor(null);

                    break;
                case R.id.top_rated_card:

                    topRated.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                    recomm.setText("Top rated books of all time");
                    forYou.setCardBackgroundColor(null);
                    bestAuthors.setCardBackgroundColor(null);


                    break;
                case R.id.best_authors_card:

                    bestAuthors.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                    recomm.setText("Books from best authors");
                    topRated.setCardBackgroundColor(null);
                    forYou.setCardBackgroundColor(null);

            }
            return true;
        }
    }
}
