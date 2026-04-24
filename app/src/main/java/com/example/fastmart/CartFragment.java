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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        ArrayList<items> cartItems = MyApplication.cart.getProducts();
        cartAdapter = new CartAdapter(getContext(), cartItems, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateTotal();
            }

            @Override
            public void onItemRemoved(items item) {
                updateTotal();
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
        String shippingStr = shippingPriceText.getText().toString().replace("$", "").trim();
        shipping = Double.parseDouble(shippingStr);
        for (items item : MyApplication.cart.getProducts()) {
            String priceStr = (item.isDotd() ? item.getNewPrice() : item.getOriginalPrice()).replace("$", "").trim();
            double price = Double.parseDouble(priceStr);
            total += price * item.getQuantity();
        }
        total += shipping;
        totalPriceText.setText("Total: $" + String.format("%.2f", total));
    }

    private void sendOrderSms() {
        StringBuilder orderDetails = new StringBuilder("Order Details:\n");
        double total = 0;
        for (items item : MyApplication.cart.getProducts()) {
            orderDetails.append(item.getName())
                    .append(" x").append(item.getQuantity())
                    .append(" - ").append(item.getNewPrice()).append("\n");
            String priceStr = item.getNewPrice().replace("$", "").trim();

            double price = Double.parseDouble(priceStr);
            total += price * item.getQuantity();
        }
        orderDetails.append("Total: $").append(String.format("%.2f", total));

        String DestinationAddress = "+923234967783";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(DestinationAddress, null, orderDetails.toString(), null, null);
    }
}