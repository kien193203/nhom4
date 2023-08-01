package FA22_PRO1121.poly.nhom4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import FA22_PRO1121.poly.nhom4.Model.Order;
import FA22_PRO1121.poly.nhom4.Model.Products;
import FA22_PRO1121.poly.nhom4.Ultils.Common;

public class ProductDetailActivityUser extends AppCompatActivity {

    ConstraintLayout container_choose_size_color;
    ImageView ic_cart, ic_back, minusBtn, plusBtn, image_product;
    TextView nameProduct_detail, priceProduct_detail, quantity, cart_count;
    RatingBar rateProduct_detail;
    Button btnAddToCart_dialog, btnBlue, btnGreen, btnRed, btnWhite, btnBlack, btnL, btnM, btnS;
    Products product;
    int numberOrder = 1;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    List<Order> userOrders = MainActivity_User.userOrders;
    String color = "", size = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_user);
        initView();
        getData();
        btnClick();
        iconClick();
        btnSizeClick();
        btnColorClick();
    }

    private void initView() {
        container_choose_size_color = findViewById(R.id.container_choose_size_color);
        ic_cart = findViewById(R.id.ic_cart);
        ic_back = findViewById(R.id.ic_back);
        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        cart_count = findViewById(R.id.cart_count);
        image_product = findViewById(R.id.image_product);
        nameProduct_detail = findViewById(R.id.nameProduct_detail);
        priceProduct_detail = findViewById(R.id.priceProduct_detail);
        quantity = findViewById(R.id.quantity);
        rateProduct_detail = findViewById(R.id.rateProduct_detail);
        btnAddToCart_dialog = findViewById(R.id.btnAddToCart_dialog);
        btnBlue = findViewById(R.id.btnBlue);
        btnGreen = findViewById(R.id.btnGreen);
        btnRed = findViewById(R.id.btnRed);
        btnWhite = findViewById(R.id.btnWhite);
        btnBlack = findViewById(R.id.btnBlack);
        btnL = findViewById(R.id.btnL);
        btnM = findViewById(R.id.btnM);
        btnS = findViewById(R.id.btnS);
        cart_count.setText(String.valueOf(userOrders.size()));
        if (userOrders.size() == 0) {
            cart_count.setVisibility(View.GONE);
        } else {
            cart_count.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        product = (Products) getIntent().getSerializableExtra("object");
        nameProduct_detail.setText(product.getName());
        priceProduct_detail.setText(decimalFormat.format(product.getPrice()) + "đ");
        Picasso.get().load(product.getImage()).into(image_product);
    }

    private void btnSizeClick() {
        btnS.setOnClickListener(v -> {
            clearChooseBtnSize();
            size = "S";
            btnS.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnS.setTextColor(getResources().getColor(R.color.white));
        });

        btnL.setOnClickListener(v -> {
            clearChooseBtnSize();
            size = "L";
            btnL.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnL.setTextColor(getResources().getColor(R.color.white));
        });

        btnM.setOnClickListener(v -> {
            clearChooseBtnSize();
            size = "M";
            btnM.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnM.setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void btnColorClick() {
        btnBlue.setOnClickListener(v -> {
            clearChooseColor();
            color = "Blue";
            btnBlue.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnBlue.setTextColor(getResources().getColor(R.color.white));
        });

        btnBlack.setOnClickListener(v -> {
            clearChooseColor();
            color = "Black";
            btnBlack.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnBlack.setTextColor(getResources().getColor(R.color.white));
        });

        btnGreen.setOnClickListener(v -> {
            clearChooseColor();
            color = "Green";
            btnGreen.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnGreen.setTextColor(getResources().getColor(R.color.white));
        });

        btnRed.setOnClickListener(v -> {
            clearChooseColor();
            color = "Red";
            btnRed.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnRed.setTextColor(getResources().getColor(R.color.white));
        });

        btnWhite.setOnClickListener(v -> {
            clearChooseColor();
            color = "White";
            btnWhite.setBackgroundResource(R.drawable.bg_button_size_color_selected);
            btnWhite.setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void btnClick() {

        btnAddToCart_dialog.setOnClickListener(v -> {
            if (validate() > 0) {
                if (userOrders.size() > 0) { // gio hang khong rong
                    boolean isExist = false;
                    for (int i = 0; i < userOrders.size(); i++) {
                        if (userOrders.get(i).getProductId().equals(product.getId())
                                && userOrders.get(i).getSize().equals(size)
                                && userOrders.get(i).getColor().equals(color)) { // đã có sản phẩm này trong giỏ
                            userOrders.get(i).setQuantity(userOrders.get(i).getQuantity() + Integer.parseInt(quantity.getText().toString()));
                            isExist = true;
                        }
                    }
                    if (!isExist) { // hàng chọn chưa có trong giỏ
                        userOrders.add(new Order(product.getId(),
                                product.getImage(),
                                product.getName(),
                                size,
                                color,
                                Integer.parseInt(quantity.getText().toString()),
                                product.getPrice()));
                    }
                } else { // gio hang dang rong
                    userOrders.add(new Order(product.getId(),
                            product.getImage(),
                            product.getName(),
                            size,
                            color,
                            Integer.parseInt(quantity.getText().toString()),
                            product.getPrice()));
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                reference.setValue(userOrders, (error, ref) -> Toast.makeText(this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show());
                cart_count.setText(String.valueOf(userOrders.size()));
                cart_count.setVisibility(View.VISIBLE);
            }
        });

        plusBtn.setOnClickListener(v -> {
            numberOrder += 1;
            quantity.setText(String.valueOf(numberOrder));
        });

        minusBtn.setOnClickListener(v -> {
            if (numberOrder > 1) {
                numberOrder -= 1;
            }
            quantity.setText(String.valueOf(numberOrder));
        });
    }

    private void iconClick() {
        ic_back.setOnClickListener(v -> finish());
        ic_cart.setOnClickListener(v -> startActivity(new Intent(ProductDetailActivityUser.this, CartActivity.class)));
    }

    private void clearChooseBtnSize() {
        size = "";
        btnL.setBackgroundResource(R.drawable.bg_button_size_color);
        btnM.setBackgroundResource(R.drawable.bg_button_size_color);
        btnS.setBackgroundResource(R.drawable.bg_button_size_color);
        btnL.setTextColor(getResources().getColor(R.color.black));
        btnM.setTextColor(getResources().getColor(R.color.black));
        btnS.setTextColor(getResources().getColor(R.color.black));
    }

    private void clearChooseColor() {
        color = "";
        btnWhite.setBackgroundResource(R.drawable.bg_button_size_color);
        btnRed.setBackgroundResource(R.drawable.bg_button_size_color);
        btnGreen.setBackgroundResource(R.drawable.bg_button_size_color);
        btnBlack.setBackgroundResource(R.drawable.bg_button_size_color);
        btnBlue.setBackgroundResource(R.drawable.bg_button_size_color);

        btnWhite.setTextColor(getResources().getColor(R.color.black));
        btnRed.setTextColor(getResources().getColor(R.color.black));
        btnGreen.setTextColor(getResources().getColor(R.color.black));
        btnBlack.setTextColor(getResources().getColor(R.color.black));
        btnBlue.setTextColor(getResources().getColor(R.color.black));
    }

    private int validate() {
        if (size.equals("")) {
            Toast.makeText(this, "Vui lòng chọn size", Toast.LENGTH_SHORT).show();
            return -1;
        }

        if (color.equals("")) {
            Toast.makeText(this, "Vui lòng chọn màu sắc", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userOrders = MainActivity_User.userOrders;
        if (userOrders.size() == 0) {
            cart_count.setVisibility(View.GONE);
        } else {
            cart_count.setVisibility(View.VISIBLE);
        }
        clearChooseColor();
        clearChooseBtnSize();
    }
}
