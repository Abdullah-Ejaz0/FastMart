package com.example.fastmart;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchView> {
    Context context;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    Set<String> searchHistorySet;
    List<String> searchHistoryList;
    public SearchAdapter(Context context, Set<String> set){
        this.context = context;
        sPref = context.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        editor = sPref.edit();
        searchHistorySet = set;
        searchHistoryList = MyApplication.favList;
    }
    @NonNull
    @Override
    public SearchView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_search_item_design, parent, false);
        return new SearchView(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchView holder, int position) {
        String search = searchHistoryList.get(position);
        holder.text.setText(search);
        holder.cancel.setOnClickListener(v -> {
            searchHistorySet.remove(search);
            searchHistoryList.remove(position);
            editor.putStringSet("Search_hash_key", searchHistorySet).commit();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, searchHistoryList.size());
        });
    }
    @Override
    public int getItemCount() {
        return searchHistoryList.size();
    }
    public void clearall(){
        searchHistoryList.clear();
        searchHistorySet.clear();
        editor.putStringSet("Search_hash_key", searchHistorySet).commit();
        notifyDataSetChanged();
    }
    public void refreshData() {
        Set<String> temp = sPref.getStringSet("Search_hash_key", new HashSet<>());
        searchHistorySet = new HashSet<>(temp);
        searchHistoryList.clear();
        searchHistoryList.addAll(searchHistorySet);
        notifyDataSetChanged();
    }
    public class SearchView extends RecyclerView.ViewHolder
    {
        TextView text;
        ImageView cancel;
        public SearchView(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.search_item);
            cancel = itemView.findViewById(R.id.delete_search_item);
        }
    }
}
