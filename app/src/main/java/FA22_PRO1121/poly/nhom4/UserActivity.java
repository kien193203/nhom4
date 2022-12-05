package FA22_PRO1121.poly.nhom4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import FA22_PRO1121.poly.nhom4.Activity.LoginActivity;
import FA22_PRO1121.poly.nhom4.Ultils.Common;

public class UserActivity extends AppCompatActivity {

    ImageView avatar_user,back;
    TextView name_user, phone_user, total_order_user;
    TextInputEditText edt_name, edt_address, edt_pass;
    Button updateInfoUser,btnLogout;
    DatabaseReference reference;
    Query orderReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        reference = FirebaseDatabase.getInstance().getReference("User").child(Common.currentUser.getPhone());
        orderReference = FirebaseDatabase.getInstance().getReference("Request").orderByChild("phone").equalTo(Common.currentUser.getPhone());
        avatar_user = findViewById(R.id.avatar_user);
        name_user = findViewById(R.id.name_user);
        btnLogout = findViewById(R.id.btnLogout);
        phone_user = findViewById(R.id.phone_user);
        back = findViewById(R.id.back);
        total_order_user = findViewById(R.id.total_order_user);
        edt_name = findViewById(R.id.edt_name_customer);
        edt_address = findViewById(R.id.edt_address);
        edt_pass = findViewById(R.id.edt_pass);
        updateInfoUser = findViewById(R.id.updateInfoUser);
        back.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                finish();
            }
        });
        updateInfoUser.setOnClickListener(v -> {
            if (edt_name.getText().toString().trim().equals("")) {
                edt_name.setError("Vui lòng nhập tên muốn thay đổi");
                return;
            }
            if (edt_address.getText().toString().trim().equals("")) {
                edt_address.setError("Vui lòng nhập địa chỉ muốn thay đổi");
                return;
            }
            if (edt_pass.getText().toString().trim().equals("")) {
                edt_pass.setError("Vui lòng nhập mật khẩu muốn thay đổi");
                return;
            }

            Common.currentUser.setName(edt_name.getText().toString());
            Common.currentUser.setAddress(edt_address.getText().toString());
            Common.currentUser.setPass(edt_pass.getText().toString());
            reference.setValue(Common.currentUser, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(UserActivity.this, "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
                        setData();
                        clearEditText();
                }
            });
        });

        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_order_user.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setData();
    }

    public void setData(){
        name_user.setText(Common.currentUser.getName());
        phone_user.setText(Common.currentUser.getPhone());
        edt_address.setText(Common.currentUser.getAddress());
        if (Common.currentUser.getAvatar() == null || Common.currentUser.getAvatar().equals("")) {
            avatar_user.setImageResource(R.drawable.avatar);
        } else {
            Picasso.get().load(Common.currentUser.getAvatar()).into(avatar_user);
        }
    }

    public void clearEditText(){
        edt_name.setText("");
        edt_pass.setText("");
    }
}