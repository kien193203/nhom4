package FA22_PRO1121.poly.nhom4.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

import FA22_PRO1121.poly.nhom4.Model.Request;
import FA22_PRO1121.poly.nhom4.R;

public class StatisticFragment extends Fragment {

    TextView total_year,total_customer,total_order,total_orderCancel,total_month,total_today;
    DatabaseReference requestReference;
    Query userReference;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistic, container, false);
        userReference = FirebaseDatabase.getInstance().getReference("User").orderByChild("role").equalTo(1);
        requestReference = FirebaseDatabase.getInstance().getReference("Request");
        total_year = root.findViewById(R.id.total_year);
        total_customer = root.findViewById(R.id.total_customer);
        total_order = root.findViewById(R.id.total_order);
        total_orderCancel = root.findViewById(R.id.total_orderCancel);
        total_month = root.findViewById(R.id.total_month);
        total_today = root.findViewById(R.id.totalToday);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total_customer.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalYear = 0;
                long totalOder = snapshot.getChildrenCount();
                int totalOrder_Cancel = 0;
                int totalMonth = 0;
                int totalToday = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Request request = dataSnapshot.getValue(Request.class);
                    if (request.getStatus() == 4){
                        totalYear += request.getTotal();
                    }

                    if (request.getStatus() ==4 && request.getDateSuccess().contains("/"+ (Calendar.getInstance().get(Calendar.MONTH)+1)+"/")){
                        totalMonth +=request.getTotal();
                        if (request.getDateSuccess().substring(0,2).equals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"")){
                            totalToday += request.getTotal();
                        }
                    }

                    if (request.getStatus() == 2){
                        totalOrder_Cancel+=1;
                    }
                }
                total_year.setText(decimalFormat.format(totalYear)+"đ");
                total_order.setText(String.valueOf(totalOder));
                total_orderCancel.setText(String.valueOf(totalOrder_Cancel));
                total_month.setText(decimalFormat.format(totalMonth)+"đ");
                total_today.setText(decimalFormat.format(totalToday));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}