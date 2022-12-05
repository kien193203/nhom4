package FA22_PRO1121.poly.nhom4.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import FA22_PRO1121.poly.nhom4.R;

public class ViewHolder_Category_admin extends RecyclerView.ViewHolder {
    public ImageView image_Category, delete, edit;
    public TextView name_Category;

    public ViewHolder_Category_admin(@NonNull View itemView) {
        super(itemView);
        image_Category = itemView.findViewById(R.id.categoryImage);
        name_Category = itemView.findViewById(R.id.categoryName);
        delete = itemView.findViewById(R.id.delete_category);
        edit = itemView.findViewById(R.id.edit_category);
    }

}
