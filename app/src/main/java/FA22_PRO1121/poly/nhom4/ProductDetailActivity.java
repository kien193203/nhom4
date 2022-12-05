package FA22_PRO1121.poly.nhom4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import FA22_PRO1121.poly.nhom4.Model.Products;

public class ProductDetailActivity extends AppCompatActivity {

    SliderView sliderView;
//    SliderAdapter sliderAdapter;
    Products product;
    Button btnBack ,btnUpdateProduct;
    TextView nameProduct_detail, priceProduct_detail, inventoryProduct_detail, descriptionProduct_detail;
    RatingBar rateProduct_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sliderView = findViewById(R.id.slider_image_detail_product);
        nameProduct_detail = findViewById(R.id.nameProduct_detail);
        priceProduct_detail = findViewById(R.id.priceProduct_detail);
        inventoryProduct_detail = findViewById(R.id.inventoryProduct_detail);
        descriptionProduct_detail = findViewById(R.id.descriptionProduct_detail);
        rateProduct_detail = findViewById(R.id.rateProduct_detail);
        btnBack = findViewById(R.id.btnBack);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        product = (Products) getIntent().getSerializableExtra("object");

        nameProduct_detail.setText(product.getName());
        priceProduct_detail.setText(product.getPrice()+"đ");
        inventoryProduct_detail.setText(String.valueOf(product.getInventory()));
//        descriptionProduct_detail.setText(product.getDescription());

//        List<String> listImage = product.getListImage();

//        sliderAdapter = new SliderAdapter(this, listImage);
//        sliderView.setSliderAdapter(sliderAdapter);
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//        sliderView.startAutoCycle();

        btnBack.setOnClickListener(v -> finish());

        btnUpdateProduct.setOnClickListener(v -> {
            Intent i = new Intent(ProductDetailActivity.this, UpdateProductActivity.class);
            i.putExtra("object_update", product);
            startActivity(i);
        });
    }
}