package FA22_PRO1121.poly.nhom4.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import FA22_PRO1121.poly.nhom4.AddProductActivity;
import FA22_PRO1121.poly.nhom4.MainActivity;
import FA22_PRO1121.poly.nhom4.Model.Categories;
import FA22_PRO1121.poly.nhom4.Model.Products;
import FA22_PRO1121.poly.nhom4.R;
import FA22_PRO1121.poly.nhom4.UpdateProductActivity;
import FA22_PRO1121.poly.nhom4.ViewHolder.ViewHolder_Category;
import FA22_PRO1121.poly.nhom4.ViewHolder.ViewHolder_Product;


public class ProductPortfolioFragment extends Fragment {

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    DatabaseReference categoryReference;
    Query productReference;
    StorageReference storageReference;
    List<ConstraintLayout> constraintLayoutList = new ArrayList<>();
    int row_index = -1;
    int old_index = -2;
    boolean check = true;

    FirebaseRecyclerOptions<Categories> optionsCategory;
    FirebaseRecyclerAdapter<Categories, ViewHolder_Category> categoriesAdapter;

    FirebaseRecyclerOptions<Products> optionsProduct;
    FirebaseRecyclerAdapter<Products, ViewHolder_Product> productAdapter;

    private TextInputEditText edt_name_category_admin;
    private ConstraintLayout container_add_category, container_chose_add;
    private TextView tv_chose_category, tv_chose_product, tv_title_product_admin;
    ImageView image_category_choose, img_product_add_avatar, img_close_chose_add_product_category, img_close_add_update_category;
    private RecyclerView recyclerView_category, recyclerView_product;

    FloatingActionButton fab;
    Categories categories;
    int id_category = 0;
    Uri selectedImageUri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.category_fragment, container, false);
        categoryReference = FirebaseDatabase.getInstance().getReference("Category");
        productReference = FirebaseDatabase.getInstance().getReference("Product");
        edt_name_category_admin = root.findViewById(R.id.edt_name_customer);
        img_product_add_avatar = root.findViewById(R.id.img_product_add_avatar);
        tv_title_product_admin = root.findViewById(R.id.tv_title_product_admin);
        image_category_choose = root.findViewById(R.id.image_category_choose);
        img_close_chose_add_product_category = root.findViewById(R.id.img_close_chose_add_product_category);
        img_close_add_update_category = root.findViewById(R.id.img_close_add_update_category);
        container_add_category = root.findViewById(R.id.container_add_category);
        container_chose_add = root.findViewById(R.id.container_chose_add);
        recyclerView_category = root.findViewById(R.id.recyclerview_category);
        recyclerView_product = root.findViewById(R.id.recyclerview_product);

        tv_chose_category = root.findViewById(R.id.tv_chose_category);
        tv_chose_product = root.findViewById(R.id.tv_chose_product);
        recyclerView_category.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView_product.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.VERTICAL,false));
        loadData();

        //swipe viewholder
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView_product);
        new ItemTouchHelper(simpleCallback2).attachToRecyclerView(recyclerView_category);

        fab = MainActivity.fab;
        fab.setOnClickListener(v -> showContainerChooseAdd());

        img_close_chose_add_product_category.setOnClickListener(v -> hideContainerChooseAdd());

        img_close_add_update_category.setOnClickListener(v -> hideContainerAddCategory());

        tv_chose_product.setOnClickListener(v -> {
            if (row_index >= 0) {
                startActivity(new Intent(getActivity(), AddProductActivity.class).putExtra("object_category", categoriesAdapter.getItem(row_index)));
            } else {
                Toast.makeText(getActivity(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            }
        });

        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    id_category = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tv_chose_category.setOnClickListener(v -> {
            showContainerAddCategory();
            hideContainerChooseAdd();
            fab.setOnClickListener(v1 -> {
                if (validate() > 0) {
                    String name = edt_name_category_admin.getText().toString();
                    if (!(name.isEmpty() && selectedImageUri != null)) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("image_category").child(selectedImageUri.getLastPathSegment());
                        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
//                            categories = new Categories(id_category + 1, name, task.getResult().toString());
                            categoryReference.child(String.valueOf(id_category + 1)).setValue(categories)
                                    .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Thêm danh mục thành công", Toast.LENGTH_SHORT).show())
                                    .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show());
                        }));

                        clearImageAndEditText();
                        hideContainerAddCategory();
                    }
                }
            });
        });

        img_product_add_avatar.setOnClickListener(v -> imageChooser());

        return root;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Bạn có chắc chắn xóa sản phẩm " + productAdapter.getItem(viewHolder.getAbsoluteAdapterPosition()).getName());
                    builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        Task<Void> task = productReference.getRef().child(String.valueOf(productAdapter.getItem(viewHolder.getAbsoluteAdapterPosition()).getId())).removeValue();
                        task.addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show());
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent i = new Intent(getContext(), UpdateProductActivity.class);
                    i.putExtra("object_update", productAdapter.getItem(viewHolder.getAbsoluteAdapterPosition()));
                    startActivity(i);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View foreGroundView = ((ViewHolder_Product) viewHolder).foreground;
//            View delete = ((ViewHolder_Product) viewHolder).layout_delete;
//            View update = ((ViewHolder_Product) viewHolder).layout_update;
//            if (dX < 0) {
//                update.setVisibility(View.GONE);
//                delete.setVisibility(View.VISIBLE);
//            } else {
//                update.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.GONE);
//            }
            getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View foreGroundView = ((ViewHolder_Product) viewHolder).foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                View foreGroundView = ((ViewHolder_Product) viewHolder).foreground;
                getDefaultUIUtil().onSelected(foreGroundView);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            View foreGroundView = ((ViewHolder_Product) viewHolder).foreground;
            getDefaultUIUtil().clearView(foreGroundView);
        }
    };
    ItemTouchHelper.SimpleCallback simpleCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch (direction) {
                case ItemTouchHelper.UP:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Bạn có chắc chắn xóa danh mục " + categoriesAdapter.getItem(viewHolder.getAbsoluteAdapterPosition()).getName());
                    builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        Task<Void> task = categoryReference.child(String.valueOf(categoriesAdapter.getItem(viewHolder.getAbsoluteAdapterPosition()).getId())).removeValue();
                        task.addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Xóa danh mục thành công", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Xóa danh mục thất bại", Toast.LENGTH_SHORT).show());
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case ItemTouchHelper.DOWN:
                    fab.setImageResource(R.drawable.ic_baseline_edit_24);
                    categories = categoriesAdapter.getItem(viewHolder.getAbsoluteAdapterPosition());
                    showContainerAddCategory();
                    tv_title_product_admin.setText("UPDATE CATEGORY");
                    Picasso.get().load(categories.getImage()).into(image_category_choose);
                    edt_name_category_admin.setText(categories.getName());
                    fab.setOnClickListener(v -> {
                        if (validate() > 0) {
                            String name = edt_name_category_admin.getText().toString();
                            categories.setName(name);
                            storageReference = FirebaseStorage.getInstance().getReference().child("image_category").child(selectedImageUri.getLastPathSegment());
                            storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnCompleteListener(task -> {
                                        categories.setImage(task.getResult().toString());
                                        categoryReference.child(String.valueOf(categories.getId())).setValue(categories)
                                                .addOnCompleteListener(task1 -> Toast.makeText(getActivity(), "Sửa danh mục thành công", Toast.LENGTH_SHORT).show())
                                                .addOnCanceledListener(() -> Toast.makeText(getActivity(), "Sửa danh mục thất bại", Toast.LENGTH_SHORT).show());
                                    }));
                            clearImageAndEditText();
                            hideContainerAddCategory();
                            tv_title_product_admin.setText("ADD CATEGORY");
                        }
                    });
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View foreGroundView = ((ViewHolder_Category) viewHolder).foreground;
            View delete = ((ViewHolder_Category) viewHolder).layout_delete;
            View update = ((ViewHolder_Category) viewHolder).layout_update;
            if (dY < 0) {
                update.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
            } else {
                update.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
            }
            getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View foreGroundView = ((ViewHolder_Category) viewHolder).foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                View foreGroundView = ((ViewHolder_Category) viewHolder).foreground;
                getDefaultUIUtil().onSelected(foreGroundView);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            View foreGroundView = ((ViewHolder_Category) viewHolder).foreground;
            getDefaultUIUtil().clearView(foreGroundView);
        }
    };


    public void updateRecyclerViewProduct(Query reference){
        optionsProduct = new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference,Products.class).build();
        productAdapter = new FirebaseRecyclerAdapter<Products, ViewHolder_Product>(optionsProduct) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Product holder, int position, @NonNull Products model) {
                holder.nameProduct.setText(model.getName());
                holder.priceProduct.setText(decimalFormat.format(model.getPrice()) + "đ");
                Picasso.get().load(model.getImage()).into(holder.imageProduct);
                holder.inventoryProduct.setText(String.valueOf(model.getInventory()));

//                holder.layout_item_product.setOnClickListener(v -> {
//                    Intent i = new Intent(getActivity(), ProductDetailActivity.class);
//                    i.putExtra("object", model);
//                    startActivity(i);
//                });
            }

            @NonNull
            @Override
            public ViewHolder_Product onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product,parent,false);
                return new ViewHolder_Product(v);
            }
        };
        productAdapter.startListening();
        recyclerView_product.setAdapter(productAdapter);
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode()
                == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                Picasso.get().load(selectedImageUri).into(image_category_choose);
            }
        }
    });

    public int validate() {
        if (selectedImageUri == null) {
            Toast.makeText(getActivity(), "Vui lòng chọn ảnh danh mục", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (edt_name_category_admin.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return 1;
    }

    public void clearImageAndEditText() {
        edt_name_category_admin.setText("");
        selectedImageUri = null;
        image_category_choose.setImageBitmap(null);
    }

    public void hideContainerChooseAdd() {
        container_chose_add.setVisibility(View.GONE);
        container_chose_add.animate().alpha(0).translationY(1000).setDuration(1000).setStartDelay(0);
    }

    public void showContainerChooseAdd() {
        container_chose_add.setVisibility(View.VISIBLE);
        container_chose_add.setAlpha(0);
        container_chose_add.setTranslationY(1500);
        container_chose_add.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(0);
    }

    public void hideContainerAddCategory() {
        fab.setImageResource(R.drawable.add);
        container_add_category.setVisibility(View.GONE);
        container_add_category.setTranslationY(0);
        container_add_category.animate().alpha(0).translationY(1000).setDuration(1000).setStartDelay(0);
        fab.setOnClickListener(v -> showContainerChooseAdd());
    }

    public void showContainerAddCategory() {
        container_add_category.setVisibility(View.VISIBLE);
        container_add_category.setAlpha(0);
        container_add_category.setTranslationY(1500);
        container_add_category.animate().alpha(1).translationY(0).setDuration(1000).setStartDelay(0);
    }

    public void loadData() {
        optionsCategory = new FirebaseRecyclerOptions.Builder<Categories>().setQuery(categoryReference, Categories.class).build();
        categoriesAdapter = new FirebaseRecyclerAdapter<Categories, ViewHolder_Category>(optionsCategory) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Category holder, int position, @NonNull Categories model) {
                if (!constraintLayoutList.contains(holder.foreground)) {
                    constraintLayoutList.add(holder.foreground);
                }
                holder.name_Category.setText(model.getName());
                Picasso.get().load(Uri.parse(model.getImage())).into(holder.image_Category);
                if (check) {
                    updateRecyclerViewProduct(productReference);
                    check = false;
                }

                holder.layout_item_category.setOnClickListener(v -> {
                    old_index = row_index;
                    row_index = holder.getLayoutPosition();
                    for (ConstraintLayout c : constraintLayoutList) {
                        c.setBackgroundResource(R.drawable.category_background);
                    }
                    if(old_index == row_index) {
                        holder.foreground.setBackgroundResource(R.drawable.category_background);
                        old_index = -2;
                        row_index = -1;
                        productReference = FirebaseDatabase.getInstance().getReference("Product");
                    }else {
                        holder.foreground.setBackgroundResource(R.drawable.category_selected_bg);
                        productReference = FirebaseDatabase.getInstance().getReference("Product").orderByChild("id_Category").equalTo(model.getId());
                    }
                    updateRecyclerViewProduct(productReference);
                });
            }

            @NonNull
            @Override
            public ViewHolder_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
                return new ViewHolder_Category(v);
            }
        };
        categoriesAdapter.startListening();
        recyclerView_category.setAdapter(categoriesAdapter);
    }

}


