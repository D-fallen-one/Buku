package com.example.shivam97.buku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AlreadyReaded extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_readed);

        searchView=findViewById(R.id.already_searchView);
        progressBar=findViewById(R.id.login_progress);
        recyclerView=findViewById(R.id.already_recycler);
        noResult=findViewById(R.id.no_result);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    public void skipActivity(View view) {
        finish();
        startActivity(new Intent(AlreadyReaded.this,MainActivity.class));
    }
}
