package FA22_PRO1121.poly.nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import FA22_PRO1121.poly.nhom4.Activity.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ImageView app_name,logo;
    private LottieAnimationView lottie;
    private final float alpha = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_name = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);
        lottie = findViewById(R.id.lottie);

        app_name.setTranslationX(2000);
        logo.setTranslationY(-2000);
        app_name.setAlpha(alpha);
        logo.setAlpha(alpha);

        app_name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        logo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        lottie.animate().translationX(2000).setDuration(3000).setStartDelay(3900);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        },4000);
    }

}