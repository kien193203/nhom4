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
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import FA22_PRO1121.poly.nhom4.Adapter.ImagesAdapter;
import FA22_PRO1121.poly.nhom4.Model.Categories;
import FA22_PRO1121.poly.nhom4.Model.Products;

public class AddProductActivity extends AppCompatActivity {
    StorageReference storageReference;
    Toolbar toolbar_add_product;
    ImageView image_choose_product;
    RecyclerView recycler_image_product;
    TextInputEditText edt_nameProduct, edt_inventoryProduct, edt_priceProduct;
    EditText edt_description_product, name_category;
    Button btnAddProduct;
    List<String> imageLinks;
    List<String> imagesProductList;
    ImagesAdapter imagesAdapter;
    Categories categories;
    int id_product = 0;
    DatabaseReference reference;
    Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        reference = FirebaseDatabase.getInstance().getReference("Product");
        edt_nameProduct = findViewById(R.id.edt_nameProduct);
        edt_priceProduct = findViewById(R.id.edt_priceProduct);
        edt_inventoryProduct = findViewById(R.id.edt_inventoryProduct);
        edt_description_product = findViewById(R.id.edt_description_product);
        btnAddProduct = findViewById(R.id.btnAddProduct);
//        name_category = findViewById(R.id.name_category);
//        toolbar_add_product = findViewById(R.id.toolbar_add_product);
        image_choose_product = findViewById(R.id.image_choose_product);
        recycler_image_product = findViewById(R.id.recycler_image_product);
        imagesProductList = new ArrayList<>();
        imageLinks = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(this, imagesProductList);
        recycler_image_product.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_image_product.setAdapter(imagesAdapter);

//        setSupportActionBar(toolbar_add_product);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Add Product");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        toolbar_add_product.setNavigationOnClickListener(v -> finish());
        Intent i = getIntent();
        categories = (Categories) i.getSerializableExtra("object_category");
        name_category.setText(categories.getName());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id_product = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAddProduct.setOnClickListener(v -> {
            if (validate() > 0) {
                String name = edt_nameProduct.getText().toString();
//                String des = edt_description_product.getText().toString();
                int price = Integer.parseInt(edt_priceProduct.getText().toString());
                int inventory = Integer.parseInt(edt_inventoryProduct.getText().toString());
                ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);
                progressDialog.setMessage("Vui lòng chờ...");
                progressDialog.show();
                if (!(imagesProductList.size() != 0 && name.isEmpty())) {
                    for (int j = 0; j < imagesProductList.size(); j++) {
                        Uri uri = Uri.parse(imagesProductList.get(j));
                        storageReference = FirebaseStorage.getInstance().getReference().child("image_product").child(System.currentTimeMillis()+uri.getLastPathSegment());
                        storageReference.putFile(uri)
                                .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl()
                                        .addOnSuccessListener(uri1 -> imageLinks.add(uri1.toString()))
                                        .addOnCompleteListener(task -> {
//                                    products = new Products(id_product + 1, name, inventory, des, price, imageLinks, categories.getId());
                                    reference.child(String.valueOf(id_product + 1)).setValue(products, (error, ref) -> {
                                        Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                        clearAll();
                                        progressDialog.dismiss();
                                        imageLinks.clear();
                                    });
                                }));
                    }
                }
            }
        });

        image_choose_product.setOnClickListener(v -> imageChooser());
    }

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
                imagesProductList.add(selectedImageUri.toString());
                imagesAdapter.notifyDataSetChanged();
            }
            if (data != null && data.getClipData() != null) {
                int countOfImages = data.getClipData().getItemCount();
                for (int i = 0; i < countOfImages; i++) {
                    Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                    imagesProductList.add(selectedImageUri.toString());
                    imagesAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    public void clearAll() {
        edt_nameProduct.setText("");
        edt_inventoryProduct.setText("");
        edt_priceProduct.setText("");
        edt_description_product.setText("");
        imagesProductList.clear();
        imagesAdapter.notifyDataSetChanged();
    }
}