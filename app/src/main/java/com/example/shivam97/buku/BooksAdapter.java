package com.example.shivam97.buku;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.shivam97.buku.Buku.mDatabase;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private Context ctx;
    private ProgressBar progressBar;
    private ArrayList<String> titles;
    private ArrayList<String> ISBN;
    private ArrayList<String> authors;
    // private ArrayList<String> ratings;
    private ArrayList<String> url;

    public void setData(ArrayList<String> titles,ArrayList<String> authors,ArrayList<String> ratings){
        this.titles = titles;
        this.authors = authors;
        //   this.ratings = ratings;
        this.notifyDataSetChanged();
    }
    public void addData(String title,String author,String rating){
        titles.add(title); authors.add(author);
        //ratings.add(rating);
        url=new ArrayList<>();
        this.notifyDataSetChanged();

    }

    void addISBN(String key,String num){
        ISBN.add(num);
        initiateFirebase(key);
    }

    BooksAdapter(Context context,ProgressBar progressBar){
        ctx= context;this.progressBar=progressBar;
        ISBN=new ArrayList<>();
        titles =new ArrayList<>();
        authors=new ArrayList<>();
        url=new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.book_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int i=holder.getAdapterPosition();
        Picasso.get().load(url.get(i)).into(holder.image);
        holder.title.setText(titles.get(i));
        holder. author.setText(authors.get(i));
        holder. rating.setText("Ratings: 4.2/5");

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,author,rating;
        ViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.book_img);
            title=itemView.findViewById(R.id.book_name);
            author=itemView.findViewById(R.id.book_author);
            rating=itemView.findViewById(R.id.book_rating);

        }

    }

    private void initiateFirebase(final String key) {

        final String[] s = new String[3];

        mDatabase.child("bookTitle").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                s[0] =dataSnapshot.getValue().toString();
                mDatabase.child("imageUrlM").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        s[1]=dataSnapshot.getValue().toString();
                        mDatabase.child("bookAuthor").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                s[2]= dataSnapshot.getValue().toString();
                                titles.add(s[0]);
                                url.add(s[1]);
                                authors.add(s[2]);
                                notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}



