package FA22_PRO1121.poly.nhom4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import FA22_PRO1121.poly.nhom4.Fragment.InfomationFragment;
import FA22_PRO1121.poly.nhom4.Fragment.OrderFragment;
import FA22_PRO1121.poly.nhom4.Fragment.ProductPortfolioFragment;

public class MainActivity extends AppCompatActivity {

    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.addBtn);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setBackground(null);
        replaceFragment(new ProductPortfolioFragment());
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.portfolioBtn:
                    replaceFragment(new ProductPortfolioFragment());
                    break;
                case R.id.infoBtn:
                    replaceFragment(new InfomationFragment());
                    break;
                case R.id.orderBtn:
                    replaceFragment(new OrderFragment());
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}