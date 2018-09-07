package com.example.shivam97.buku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.example.shivam97.buku.Buku.mAuth;
import static com.example.shivam97.buku.Buku.username;
import static com.example.shivam97.buku.Buku.mDatabase;

public class BookDeatails extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener{

    String ratingurl ="http://igi627.000webhostapp.com/buku/rateBook.php";
    RequestQueue requestQueue;
    String isratedurl="http://igi627.000webhostapp.com/buku/requestRating.php";
    RatingBar ratingBar;
    TextView ratingView,addedTo,bookName,bookAuthor,book_detail;
    ImageView bookImage;
    RecyclerView alsoReadRecycler;
    String bookIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_deatails);

        String title,author,rating;
        title=getIntent().getStringExtra("title");
        author=getIntent().getStringExtra("author");
        rating=getIntent().getStringExtra("rating");
        bookIndex=getIntent().getStringExtra("index");

        ratingBar=findViewById(R.id.rating_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingView=findViewById(R.id.rating_view);
        addedTo=findViewById(R.id.added_to_wishlist);
        addedTo.setVisibility(View.GONE);
        bookName=findViewById(R.id.detail_book_name);
        bookAuthor=findViewById(R.id.detail_book_author);
        bookImage=findViewById(R.id.detail_imageView);
        book_detail=findViewById(R.id.detail_desc);
        alsoReadRecycler=findViewById(R.id.read_recycler);
        bookName.setText(title);
        bookAuthor.setText(author);
        ratingView.append(rating);
        ratingBar.setOnRatingBarChangeListener(this);
        Picasso.get().load(getIntent().getStringExtra("url")).networkPolicy(NetworkPolicy.OFFLINE).into(bookImage);
        requestQueue= Volley.newRequestQueue(this);
        isRated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addToWishList(View view){
        addedTo.setVisibility(View.VISIBLE);
        view.setAlpha(0.5f);
        view.setEnabled(false);
        ratingBar.setEnabled(false);

    }

    private void isRated(){

       StringRequest stringRequest=new StringRequest(Request.Method.POST, isratedurl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Toast.makeText(BookDeatails.this,response,Toast.LENGTH_LONG).show();
               Log.i("responseRecieved",response);
//               Float f=Float.parseFloat(response);
//                f/=2.0f;
//                ratingBar.setRating(f);
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params=new HashMap<>();
               params.put("userID",username);
               params.put("ISBN",bookIndex);
               return params;
           }
       };
       requestQueue.add(stringRequest);
    }
    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

        (findViewById(R.id.btn_add)).setEnabled(false);
        final String rate=String.valueOf(v*2);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue().toString();
                execute(rate,username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void execute(final String rating, final String username) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ratingurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response of ratings",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters=new HashMap<>();
                parameters.put("userID",username);
                parameters.put("ISBN",bookIndex);
                parameters.put("bookRating",rating);
                return parameters;
            }
        };

        requestQueue.add(stringRequest);
    }
}
