package FA22_PRO1121.poly.nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import FA22_PRO1121.poly.nhom4.Activity.LoginActivity;
import FA22_PRO1121.poly.nhom4.Fragment.Category_Fragment;
import FA22_PRO1121.poly.nhom4.Fragment.CustomerFragment;
import FA22_PRO1121.poly.nhom4.Fragment.InfomationFragment;
import FA22_PRO1121.poly.nhom4.Fragment.OrderFragment;
import FA22_PRO1121.poly.nhom4.Fragment.ProductFragment;
import FA22_PRO1121.poly.nhom4.Fragment.StatisticFragment;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new InfomationFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                replaceFragment(new InfomationFragment());
                break;
            case R.id.nav_category:
                replaceFragment(new Category_Fragment());
                break;
            case R.id.nav_product:
                replaceFragment(new ProductFragment());
                break;
            case R.id.nav_customer:
                replaceFragment(new CustomerFragment());
                break;
            case R.id.nav_order:
                replaceFragment(new OrderFragment());
                break;
            case R.id.nav_statistic:
                replaceFragment(new StatisticFragment());
                break;
            case R.id.nav_logout:
                NavigationDrawer.this.finishAffinity();
                startActivity(new Intent(NavigationDrawer.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START,true);
        return false;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}