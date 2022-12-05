package FA22_PRO1121.poly.nhom4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import FA22_PRO1121.poly.nhom4.Adapter.ImagesAdapter;
import FA22_PRO1121.poly.nhom4.Model.Products;


public class UpdateProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycler_image_product;
    ImageView image_choose_product;
    Button btnUpdateProduct;
    TextInputEditText edt_nameProduct, edt_priceProduct, edt_inventoryProduct;
    EditText edt_description_product;
    public List<String> imagesProductList;
    List<String> imageLinksNew;
    ImagesAdapter imagesAdapter;
    Products products;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        toolbar = findViewById(R.id.toolbar_update_product);
        recycler_image_product = findViewById(R.id.recycler_image_product);
        image_choose_product = findViewById(R.id.image_choose_product);
        edt_nameProduct = findViewById(R.id.edt_nameProduct);
        edt_priceProduct = findViewById(R.id.edt_priceProduct);
        edt_inventoryProduct = findViewById(R.id.edt_inventoryProduct);
        edt_description_product = findViewById(R.id.edt_description_product);
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        products = (Products) getIntent().getSerializableExtra("object_update");
        imageLinksNew = new ArrayList<>();
//        imagesProductList = products.getListImage();
        edt_nameProduct.setText(products.getName());
        edt_priceProduct.setText(products.getPrice() + "");
        edt_inventoryProduct.setText(products.getInventory() + "");
//        edt_description_product.setText(products.getDescription());
        recycler_image_product.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesAdapter = new ImagesAdapter(this, imagesProductList);
        recycler_image_product.setAdapter(imagesAdapter);

        btnUpdateProduct.setOnClickListener(v -> {
            if (validate() > 0) {
                String name = edt_nameProduct.getText().toString();
                String des = edt_description_product.getText().toString();
                int price = Integer.parseInt(edt_priceProduct.getText().toString());
                int inventory = Integer.parseInt(edt_inventoryProduct.getText().toString());
                ProgressDialog progressDialog = new ProgressDialog(UpdateProductActivity.this);
                progressDialog.setMessage("Vui lòng chờ...");
                progressDialog.show();
                if (!(imagesProductList.size() != 0 && name.isEmpty())) {
                    for (int j = 0; j < imagesProductList.size(); j++) {
                        if (imagesProductList.get(j).contains("https")) {
                            imageLinksNew.add(imagesProductList.get(j));
                        } else {
                            Uri uri = Uri.parse(imagesProductList.get(j));
                            storageReference = FirebaseStorage.getInstance().getReference().child("image_product" + products.getId()).child(System.currentTimeMillis() + uri.getLastPathSegment());
                            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnSuccessListener(uri1 -> imageLinksNew.add(uri1.toString()))
                                    .addOnCompleteListener(task -> {
                                        products.setName(name);
                                        products.setPrice(price);
                                        products.setInventory(inventory);
//                                        products.setListImage(imageLinksNew);
                                        databaseReference.child(String.valueOf(products.getId())).setValue(products, (error, ref) -> {
                                            Toast.makeText(UpdateProductActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                            imageLinksNew.clear();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(UpdateProductActivity.this, MainActivity.class));
                                        });
                                    }));
                        }
                    }
                }
            }
        });

        image_choose_product.setOnClickListener(v -> imageChooser());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Update Product");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imagesProductList.add(String.valueOf(selectedImageUri));
                imagesAdapter.notifyDataSetChanged();
            }
            if (data != null && data.getClipData() != null) {
                int countOfImages = data.getClipData().getItemCount();
                for (int i = 0; i < countOfImages; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    imagesProductList.add(String.valueOf(selectedImageUri));
                    imagesAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    public int validate() {
        if (edt_nameProduct.getText().toString().trim().isEmpty() ||
                edt_priceProduct.getText().toString().trim().isEmpty() ||
                edt_inventoryProduct.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ trường dữ liệu", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Integer.parseInt(edt_priceProduct.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Giá phải nhập số", Toast.LENGTH_SHORT).show();
            return -1;
        }

        try {
            Integer.parseInt(edt_inventoryProduct.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Số lượng phải là số nguyên", Toast.LENGTH_SHORT).show();
            return -1;
        }

        if (imagesProductList.size() == 0) {
            Toast.makeText(this, "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return 1;
    }

}