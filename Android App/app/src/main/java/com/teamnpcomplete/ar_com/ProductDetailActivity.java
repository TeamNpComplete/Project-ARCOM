package com.teamnpcomplete.ar_com;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamnpcomplete.ar_com.Classes.Product;
import com.teamnpcomplete.ar_com.adapters.ProductPageAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailActivity extends AppCompatActivity {

    ViewPager pager;
    int currentPage = 0;

    SearchView searchView;

    DatabaseReference fDatabase;
    String productId;

    TextView productName;
    TextView productPrice;
    RatingBar productRatingBar;
    TextView productRating;
    TextView colorDetails;
    TextView sizeDetails;
    TextView productDescription;
    FloatingActionButton showIn3D;

    ShimmerFrameLayout shimmerFrameLayout;

    public void init(Product product){
        pager = findViewById(R.id.view_pager_details);
        final ArrayList<String> imageUrls = new ArrayList<>(product.getImageURLs());
        ProductPageAdapter adapter = new ProductPageAdapter(ProductDetailActivity.this, imageUrls);
        pager.setAdapter(adapter);

        shimmerFrameLayout = findViewById(R.id.shimmer_layout);

        CircleIndicator indicator = findViewById(R.id.circle_indicator_details);
        indicator.setViewPager(pager);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == imageUrls.size()){
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_button));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        productId = getIntent().getStringExtra("productId");

        fDatabase = FirebaseDatabase.getInstance().getReference("Product");

        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productRatingBar = findViewById(R.id.product_rating_bar);
        productRating = findViewById(R.id.product_rating);
        colorDetails = findViewById(R.id.product_color);
        sizeDetails = findViewById(R.id.product_size);
        productDescription = findViewById(R.id.product_description);
        showIn3D = findViewById(R.id.show_in_3d);

        fetchDetails();
    }

    public void goToUnity(View view){
        FirebaseDatabase.getInstance().getReference("Product")
                .child(productId)
                .child("assetPath")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String assetPath = dataSnapshot.getValue(String.class);

                        String assetName = assetPath.split("/")[0];
//
//                        Intent intent = new Intent(ProductDetailActivity.this, MiddleActivity.class);
//                        intent.putExtra("host", "http://192.168.1.21:8080/");
//                        intent.putExtra("assetPath", assetPath);
//                        intent.putExtra("propertiesPath", assetName);
//                        startActivity(intent);



//                        String assetPath = getIntent().getStringExtra("assetPath");
//                        String assetName = getIntent().getStringExtra("propertiesPath");


                        Intent intent = new Intent(ProductDetailActivity.this, UnityPlayerActivity.class);

                        intent.putExtra("arguments", true);
                        intent.putExtra("host", "http://arcomm.herokuapp.com/");
                        intent.putExtra("assetPath", "assets/"+assetPath);
                        intent.putExtra("propertiesPath", "properties/"+assetName);
                        //Toast.makeText(this, ""+ assetPath, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+ assetName, Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void fetchDetails() {

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    Product product = child.getValue(Product.class);
                    if(product.getId().equals(productId))
                    {

                        init(product);

                        productName.setText(product.getName() + product.getShortDesc());
                        productPrice.setText("INR " + product.getPrice());
                        productRatingBar.setRating((float)product.getRating());
                        productRating.setText(String.valueOf(product.getRating()));
                        colorDetails.setText(product.getDetails().get("Color"));
                        sizeDetails.setText(product.getDetails().get("Size"));
                        productDescription.setText(product.getDescription());

                        shimmerFrameLayout.setVisibility(View.GONE);

                        break;
                    }
                }

                fDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_cart:
                Toast.makeText(this, "Cart Clicked !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(this, "Search Clicked !", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
