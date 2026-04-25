package com.example.fastmart;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    TextView totalPriceText, shippingPriceText;
    Button checkoutButton;
    CartAdapter cartAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public CartFragment() {
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyApplication.cartFragment = this;
        recyclerView = view.findViewById(R.id.rv_cart);
        totalPriceText = view.findViewById(R.id.cart_total);
        shippingPriceText = view.findViewById(R.id.cart_shipping);
        checkoutButton = view.findViewById(R.id.checkoutbtn);
        cartAdapter = new CartAdapter(getContext(), MyApplication.cartDB.getAllCartItems(), new CartAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateTotal();
            }
            @Override
            public void onItemRemoved(items item) {
                updateTotal();
                MyApplication.updateCart();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);
        updateTotal();
        checkoutButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendOrderSms();
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        101);
            }
        });
    }
    public void updateTotal() {
        double total = 0, shipping;
        try {
            String shippingStr = shippingPriceText.getText().toString().replace("$", "").trim();
            shipping = Double.parseDouble(shippingStr);
        } catch (Exception e) {
            shipping = 0;
        }
        ArrayList<items> items = MyApplication.cartDB.getAllCartItems();
        for (items item : items) {
            try {
                String priceStr = (item.isDotd() ? item.getNewPrice() : item.getOriginalPrice()).replace("$", "").trim();
                double price = Double.parseDouble(priceStr);
                total += price * item.getQuantity();
            } catch (Exception e) {}
        }
        total += shipping;
        totalPriceText.setText("Total: $" + String.format("%.2f", total));
    }
    private void sendOrderSms() {
        ArrayList<items> cartItems = MyApplication.cartDB.getAllCartItems();
        if (cartItems.isEmpty()) {
            return;
        }
        StringBuilder orderDetails = new StringBuilder("Order Details:\n");
        double total = 0;
        for (items item : cartItems) {
            String priceStr = (item.isDotd() ? item.getNewPrice() : item.getOriginalPrice()).replace("$", "").trim();
            double price = Double.parseDouble(priceStr);
            orderDetails.append(item.getName())
                    .append(" x").append(item.getQuantity())
                    .append(" - $").append(String.format("%.2f", price * item.getQuantity())).append("\n");
            total += price * item.getQuantity();
        }
        orderDetails.append("Total: $").append(String.format("%.2f", total));
        String DestinationAddress = "+923234967783";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(DestinationAddress, null, orderDetails.toString(), null, null);
        saveOrderToFirebase(cartItems, total);
        MyApplication.cartDB.clearCart();
        MyApplication.updateCart();
    }
    private void saveOrderToFirebase(ArrayList<items> items, double total) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("OrderHistory");
        String orderId = dbRef.push().getKey();
        String date = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
        String customerName = MyApplication.user != null ? MyApplication.user.getName() : "Guest";
        Order order = new Order(orderId, date, "$" + String.format("%.2f", total), customerName, items);
        if (orderId != null) {
            dbRef.child(orderId).setValue(order);
        }
    }
}
