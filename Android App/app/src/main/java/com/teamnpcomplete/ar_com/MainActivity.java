package com.teamnpcomplete.ar_com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamnpcomplete.ar_com.Classes.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import com.google.android.material.navigation.NavigationView;
import com.teamnpcomplete.ar_com.adapters.ProductAdapterDataHolder;
import com.teamnpcomplete.ar_com.adapters.ProductPageAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager pager;
    ArrayList<String> imageUrls = new ArrayList<>();
    int currentPage = 0;

    ArrayList<Product> productArrayList;
    DatabaseReference fDatabase;
    RecyclerView dataRecyclerView;
    HashMap<String, String> details;

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fDatabase = FirebaseDatabase.getInstance().getReference("Product");
        dataRecyclerView = findViewById(R.id.products_list_recycler);
        productArrayList = new ArrayList<Product>();

        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        layoutManager.setReverseLayout( true );
        layoutManager.setStackFromEnd( true );
        dataRecyclerView.setHasFixedSize( true );
        dataRecyclerView.setLayoutManager( layoutManager );
        dataRecyclerView.setNestedScrollingEnabled(false);

        shimmerFrameLayout = findViewById(R.id.shimmer_layout);

        init();

        retrieveFromDatabase();

    }

    public void init(){
        pager = findViewById(R.id.view_pager_home);
        imageUrls.add("https://rukminim1.flixcart.com/image/800/960/k1v1h8w0/shoe/z/y/8/cm4547-8-adidas-carbon-carbon-original-imafhcgf3x2ghhw3.jpeg?q=50");
        imageUrls.add("https://rukminim1.flixcart.com/image/800/960/k1v1h8w0/shoe/z/y/8/cm4547-8-adidas-carbon-carbon-original-imafhcgfah2kw52z.jpeg?q=50");
        imageUrls.add("https://rukminim1.flixcart.com/image/800/960/k1v1h8w0/shoe/z/y/8/cm4547-8-adidas-carbon-carbon-original-imafhcgfgt8f8keb.jpeg?q=50");
        imageUrls.add("https://rukminim1.flixcart.com/image/800/960/k1v1h8w0/shoe/z/y/8/cm4547-8-adidas-carbon-carbon-original-imafhcgfhdug5bzt.jpeg?q=50");
        ProductPageAdapter adapter = new ProductPageAdapter(MainActivity.this, imageUrls);
        pager.setAdapter(adapter);

        CircleIndicator indicator = findViewById(R.id.circle_indicator_home);
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

    private void retrieveFromDatabase() {
        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for( DataSnapshot child : dataSnapshot.getChildren()){
                    Product product = child.getValue(Product.class);

                    try {
                        productArrayList.add(product);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                }

                ProductAdapterDataHolder productAdapterDataHolder = new ProductAdapterDataHolder(MainActivity.this, productArrayList, dataRecyclerView);
                dataRecyclerView.setAdapter(productAdapterDataHolder);

                fDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent(MainActivity.this, CategoryWiseDisplayActivity.class);
        if (id == R.id.nav_electronics) {
            intent.putExtra("category", "Electronics");
            startActivity(intent);

        } else if (id == R.id.nav_television) {
            intent.putExtra("category", "Television");
            startActivity(intent);

        } else if (id == R.id.nav_fashion) {
            intent.putExtra("category", "Fashion");
            startActivity(intent);

        } else if (id == R.id.nav_furniture) {
            intent.putExtra("category", "Furniture");
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
