package com.example.fastmart;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.annotation.NonNull;

public class ItemList {
    private final ArrayList<items> products;

    ItemList(){
        products = new ArrayList<>();
    }
    public void markFavourite(Set<String> favs){
        for(String model:favs){
            items Item =this.getProduct(model);
            if(Item != null){
                Item.setFavourite(true);
            }
        }
    }

    public ArrayList<items> getFavourites(){
        ArrayList<items> favourites = new ArrayList<>();
        for(items item: products){
            if(item.isFavourite()){
                favourites.add(item);
            }
        }
        return favourites;
    }
    public void populate() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("products");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    items item = data.getValue(items.class);
                    if (item != null) {
                        if (MyApplication.favModels.contains(item.getModel())) {
                            item.setFavourite(true);
                        }
                        products.add(item);
                    }
                }
                MyApplication.notifyFavouritesChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void populatelocal() {

        String name, model, newPrice, originalPrice, description, sDesc, type;
        int img;

        int count = 0; // controls DOTD

        // Product 1
        name = "RØDE PodMic";
        model = "PodMic";
        newPrice = "$108.20";
        originalPrice = "$129.00";
        img = R.drawable.rode_mic;
        description = "RØDE PodMic is a professional dynamic microphone designed for podcasting and broadcast-quality audio.\n" +
                "It features an internal pop filter and balanced sound profile for clear, natural voice recording.\n" +
                "Durable and compact, it delivers consistent performance for home studios and on-air setups.";
        sDesc = "Dynamic Microphone";
        type = "Microphone";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 2
        name = "SONY Premium Wireless Headphones";
        model = "WH-1000XM4, Black";
        newPrice = "$349.99";
        originalPrice = "$399.99";
        img = R.drawable.headphone_black;
        description = "Sony WH-1000XM4 Premium Wireless Headphones feature industry-leading noise cancellation for an immersive listening experience.\n" +
                "They deliver high-quality sound with deep bass and clear vocals for music and calls.\n" +
                "The headphones provide up to 30 hours of battery life with a comfortable wireless design for all-day use.";
        sDesc = "Noise Cancelling Headphones";
        type = "Headphones";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 3
        name = "SONY Premium Wireless Headphones";
        model = "WH-1000XM4, Beige";
        newPrice = "$349.99";
        originalPrice = "$399.99";
        img = R.drawable.headphone_beige;
        description = "Sony WH-1000XM4 Premium Wireless Headphones feature industry-leading noise cancellation for an immersive listening experience.\n" +
                "They deliver high-quality sound with deep bass and clear vocals for music and calls.\n" +
                "The headphones provide up to 30 hours of battery life with a comfortable wireless design for all-day use.";
        sDesc = "Noise Cancelling Headphones";
        type = "Headphones";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 4
        name = "Sony WF-1000XM6";
        model = "WF1000XM6/SZE";
        newPrice = "$299.99";
        originalPrice = "$349.99";
        img = R.drawable.wf_1000xm6;
        description = "Sony WF-1000XM6 wireless earbuds deliver detailed, high-fidelity sound with advanced noise cancellation.\n" +
                "Designed with ergonomic comfort, they provide a secure fit for long listening sessions.\n" +
                "Perfect for immersive music, calls, and everyday use.";
        sDesc = "Wireless Noise Cancelling Earbuds";
        type = "Earbuds";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 5
        name = "Sony WH-1000XM6";
        model = "WH-1000XM6";
        newPrice = "$399.99";
        originalPrice = "$449.99";
        img = R.drawable.wh_1000xm6;
        description = "Sony WH-1000XM6 headphones feature next-gen noise cancelling powered by advanced processors.\n" +
                "Enjoy premium sound quality with deep immersion and crystal-clear calls.\n" +
                "Designed for uninterrupted listening with industry-leading comfort.";
        sDesc = "Wireless Noise Cancelling Headphones";
        type = "Headphones";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 6
        name = "Sony IER-EX15C";
        model = "IER-EX15C/PZE";
        newPrice = "$39.99";
        originalPrice = "$49.99";
        img = R.drawable.ier_ex15c;
        description = "Sony IER-EX15C USB-C wired earphones offer instant plug-and-play convenience with no charging required.\n" +
                "They deliver clear vocals and strong bass despite their compact size.\n" +
                "Includes in-line controls for volume, mute, and playback for easy daily use.";
        sDesc = "USB-C Wired In-Ear Earphones";
        type = "Earphones";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 7
        name = "Sony ULT FIELD 7";
        model = "SRSULT70";
        newPrice = "$449.00";
        originalPrice = "$499.00";
        img = R.drawable.ult_field_7;
        description = "Sony ULT FIELD 7 portable speaker delivers powerful bass with dynamic lighting.\n" +
                "Built with waterproof and dustproof design for outdoor use.\n" +
                "Long battery life and microphone support make it ideal for parties and karaoke.";
        sDesc = "Wireless Portable Speaker";
        type = "Speaker";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 8
        name = "Sony ULT TOWER 9AC";
        model = "SRSULT900AC";
        newPrice = "$1099.00";
        originalPrice = "$1299.00";
        img = R.drawable.ult_tower_9ac;
        description = "Sony ULT TOWER 9AC delivers massive bass and 360° sound for a concert-like experience.\n" +
                "Includes party lighting, guitar and mic input for karaoke.\n" +
                "Perfect for large gatherings and immersive entertainment setups.";
        sDesc = "Party Speaker System";
        type = "Speaker";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 9
        name = "Sony Alpha 7R IV";
        model = "ILCE-7RM4A/QAP2";
        newPrice = "$3699.00";
        originalPrice = "$3999.00";
        img = R.drawable.a7r_iv;
        description = "Sony Alpha 7R IV delivers ultra-high 61MP resolution with fast image processing.\n" +
                "Designed for professionals, it offers exceptional detail and reliability.\n" +
                "Advanced connectivity and performance enable next-level photography.";
        sDesc = "Full Frame Mirrorless Camera";
        type = "Camera";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 10
        name = "Sony Alpha 1";
        model = "ILCE-1/BQ AP2";
        newPrice = "$6499.00";
        originalPrice = "$6999.00";
        img = R.drawable.a1;
        description = "Sony Alpha 1 combines high resolution with incredible speed for unmatched performance.\n" +
                "Offers advanced imaging capabilities with intuitive controls.\n" +
                "Ideal for professionals seeking ultimate creative flexibility.";
        sDesc = "Flagship Mirrorless Camera";
        type = "Camera";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 11
        name = "Sony FE 24-70mm F2.8 GM";
        model = "SEL2470GM//QSYX";
        newPrice = "$2099.00";
        originalPrice = "$2299.00";
        img = R.drawable.fe_24_70;
        description = "Premium G Master lens with exceptional sharpness and smooth bokeh.\n" +
                "Features Nano AR coating to reduce flare and ghosting.\n" +
                "Fast and precise autofocus with durable weather-resistant design.";
        sDesc = "Standard Zoom Lens";
        type = "Lens";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 12
        name = "Sony FE 70-200mm F4 Macro G OSS II";
        model = "SEL70200G2/CSYX";
        newPrice = "$1999.00";
        originalPrice = "$2199.00";
        img = R.drawable.fe_70_200;
        description = "Compact telephoto zoom lens with excellent optical performance.\n" +
                "Supports half-macro shooting across the zoom range.\n" +
                "Lightweight design makes it ideal for both photography and video.";
        sDesc = "Telephoto Zoom Lens";
        type = "Lens";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 13
        name = "Sony AX43A 4K Handycam";
        model = "FDR-AX43A/BCAU2";
        newPrice = "$999.00";
        originalPrice = "$1199.00";
        img = R.drawable.ax43a;
        description = "Sony AX43A Handycam captures stunning 4K video with advanced stabilization.\n" +
                "Features 20x optical zoom and fast intelligent autofocus.\n" +
                "Compact and powerful, ideal for content creators and everyday recording.";
        sDesc = "4K Camcorder";
        type = "Camera";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 14
        name = "Sony BRAVIA 8 II OLED TV";
        model = "XR BRAVIA 8 II";
        newPrice = "$3499.00";
        originalPrice = "$3999.00";
        img = R.drawable.bravia_8_ii;
        description = "Sony BRAVIA 8 II OLED TV delivers stunning 4K visuals with vibrant colors and deep contrast.\n" +
                "Powered by the XR Processor for enhanced picture quality.\n" +
                "Slim, elegant design makes it the perfect centerpiece for any home.";
        sDesc = "4K OLED Smart TV";
        type = "TV";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 15
        name = "Sony HT-A9 Home Theater System";
        model = "HT-A9//M1 AF1";
        newPrice = "$1999.99";
        originalPrice = "$2499.99";
        img = R.drawable.ht_a9;
        description = "Sony HT-A9 delivers immersive 360 Spatial Sound Mapping for a cinematic experience.\n" +
                "Wireless connectivity allows flexible speaker placement.\n" +
                "Transforms regular audio into multi-dimensional surround sound.";
        sDesc = "Dolby Atmos Home Theater";
        type = "Home Theater";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 16
        name = "Sony BRAVIA Theatre System 6";
        model = "HT-S60";
        newPrice = "$799.99";
        originalPrice = "$999.99";
        img = R.drawable.ht_s60;
        description = "Sony HT-S60 offers powerful 5.1 channel surround sound with Dolby Atmos and DTS:X.\n" +
                "Includes soundbar, subwoofer, and wireless rear speakers.\n" +
                "Quick setup delivers cinematic audio in minutes.";
        sDesc = "5.1ch Home Theatre System";
        type = "Home Theater";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 17
        name = "Sony ULT FIELD 5";
        model = "SRS-ULT50/WZE";
        newPrice = "$399.99";
        originalPrice = "$449.99";
        img = R.drawable.ult_field_5;
        description = "Sony ULT FIELD 5 portable speaker delivers deep bass and powerful sound.\n" +
                "Features 360° party lighting for an enhanced music experience.\n" +
                "Portable design lets you take the party anywhere.";
        sDesc = "Wireless Portable Speaker";
        type = "Speaker";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 18
        name = "Sony UBP-X800M2 Blu-ray Player";
        model = "UBP-X800M2";
        newPrice = "$329.99";
        originalPrice = "$399.99";
        img = R.drawable.ubp_x800m2;
        description = "Sony UBP-X800M2 delivers stunning 4K Ultra HD Blu-ray playback.\n" +
                "Supports Dolby Atmos, HDR, and high-resolution audio.\n" +
                "Anti-vibration design ensures clear and stable sound quality.";
        sDesc = "4K Ultra HD Blu-ray Player";
        type = "Media Player";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 19
        name = "PlayStation 5 Pro";
        model = "PS5 Pro 2TB";
        newPrice = "$749.00";
        originalPrice = "$799.00";
        img = R.drawable.ps5_pro;
        description = "PlayStation 5 Pro delivers enhanced gaming with advanced ray tracing and 4K clarity.\n" +
                "Experience smoother gameplay with higher frame rates and stunning visuals.\n" +
                "All-digital console designed for next-gen immersive gaming.";
        sDesc = "Next-Gen Gaming Console";
        type = "Gaming";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 20
        name = "Sony DualSense Wireless Controller";
        model = "DualSense";
        newPrice = "$74.00";
        originalPrice = "$79.00";
        img = R.drawable.dualsense;
        description = "DualSense controller provides immersive haptic feedback and adaptive triggers.\n" +
                "Enhances gameplay with responsive controls and ergonomic design.\n" +
                "Feel every action directly in your hands.";
        sDesc = "Wireless Gaming Controller";
        type = "Gaming";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 21
        name = "Sony PULSE Elite Wireless Headset";
        model = "PULSE Elite";
        newPrice = "$149.00";
        originalPrice = "$169.00";
        img = R.drawable.pulse_elite;
        description = "PULSE Elite headset delivers high-quality gaming audio with planar magnetic drivers.\n" +
                "Features AI-enhanced noise reduction and fast wireless connectivity.\n" +
                "Long battery life ensures uninterrupted gaming sessions.";
        sDesc = "Wireless Gaming Headset";
        type = "Gaming";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 22
        name = "Sony INZONE H9 Gaming Headset";
        model = "WH-G900N/WZ E";
        newPrice = "$299.99";
        originalPrice = "$349.99";
        img = R.drawable.inzone_h9;
        description = "INZONE H9 gaming headset offers 360 spatial sound and noise cancellation.\n" +
                "Designed for comfort during long gaming sessions.\n" +
                "Includes a flip-up microphone and customizable audio settings.";
        sDesc = "Noise Cancelling Gaming Headset";
        type = "Gaming";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));

        // Product 23
        name = "Sony Xperia 1 VII";
        model = "Xperia 1 VII";
        newPrice = "$1299.99";
        originalPrice = "$1399.99";
        img = R.drawable.xperia_1_vii;
        description = "Sony Xperia 1 VII features advanced AI camerawork and ultra-wide sensor.\n" +
                "Powered by Snapdragon 8 Elite for high performance.\n" +
                "Offers immersive display, high-quality audio, and long battery life.";
        sDesc = "Flagship Smartphone";
        type = "Mobile";
        products.add(new items(name, model, newPrice, originalPrice, description, sDesc, img, type, count++ < 3));
    }

    items getProduct(String model){
        for(items product: products){
            if (Objects.equals(model, product.model)){
                return product;
            }
        }
        return null;
    }

    public void addItem(items item){
        products.add(item);
    }

    public void removeItem(String Model){
        products.removeIf(product -> product.model.equals(Model));
    }

    public ArrayList<items> getDotdProducts() {
        ArrayList<items> dotdList = new ArrayList<>();
        for (items item : products) {
            if (item.isDotd()) {
                dotdList.add(item);
            }
        }
        return dotdList;
    }

    public ArrayList<items> getRecomProducts() {
        ArrayList<items> recomList = new ArrayList<>();
        for (items item : products) {
            if (!item.isDotd()) {
                recomList.add(item);
            }
        }
        return recomList;
    }

    public ArrayList<items> getProducts() {
        return products;
    }
}
