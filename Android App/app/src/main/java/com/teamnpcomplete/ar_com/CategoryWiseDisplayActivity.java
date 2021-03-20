package com.teamnpcomplete.ar_com;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamnpcomplete.ar_com.Classes.Product;
import com.teamnpcomplete.ar_com.adapters.ProductAdapterDataHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryWiseDisplayActivity extends AppCompatActivity {

    DatabaseReference fDatabase;
    ArrayList<Product> catList;
    RecyclerView recyclerView;

    String productCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_display);

        productCategory = getIntent().getStringExtra("category");
        Log.e("Receiving ", productCategory);

        fDatabase = FirebaseDatabase.getInstance().getReference("Product");
        catList = new ArrayList<>();
        recyclerView = findViewById(R.id.category_wise_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( layoutManager );

        fetchFromDatabase();
    }

    private void fetchFromDatabase(){

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Product product = child.getValue(Product.class);

                    if(product.getCategory().equals(productCategory)){
                        catList.add(product);
                    }
                }

                ProductAdapterDataHolder adapterDataHolder = new ProductAdapterDataHolder(CategoryWiseDisplayActivity.this, catList, recyclerView);
                recyclerView.setAdapter(adapterDataHolder);

                fDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
