package FA22_PRO1121.poly.nhom4.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import FA22_PRO1121.poly.nhom4.Interface.ItemClickListener;
import FA22_PRO1121.poly.nhom4.R;

public class ViewHolder_Order extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView name_order, status_order,size_product,color_product,price_product_order,quantity_order,name_product_in_order;
    public ImageView image_product;
    ItemClickListener itemClickListener;
    public ViewHolder_Order(@NonNull View itemView) {
        super(itemView);
        name_order = itemView.findViewById(R.id.name_order);
        status_order = itemView.findViewById(R.id.status_order);
        size_product = itemView.findViewById(R.id.size_product);
        color_product = itemView.findViewById(R.id.color_product);
        price_product_order = itemView.findViewById(R.id.price_product_order);
        quantity_order = itemView.findViewById(R.id.quantity_order);
        name_product_in_order = itemView.findViewById(R.id.name_product_in_order);
        image_product = itemView.findViewById(R.id.image_product);

        itemView.setOnClickListener(this);

        itemView.setOnCreateContextMenuListener(this);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Thay đổi trạng thái hóa đơn");
        menu.add(0, 0, getAbsoluteAdapterPosition(), "Cập nhật");
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAbsoluteAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
