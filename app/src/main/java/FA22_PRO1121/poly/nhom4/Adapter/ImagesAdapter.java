package FA22_PRO1121.poly.nhom4.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import FA22_PRO1121.poly.nhom4.R;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    Context context;
    List<String> list;

    public ImagesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_image_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position)).into(holder.image_choose_product_item);
        holder.delete_image_product.setOnClickListener(v -> {
            list.remove(holder.getAbsoluteAdapterPosition());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_choose_product_item, delete_image_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_choose_product_item = itemView.findViewById(R.id.image_choose_product_item);
            delete_image_product = itemView.findViewById(R.id.delete_image_product);
        }
    }
}
