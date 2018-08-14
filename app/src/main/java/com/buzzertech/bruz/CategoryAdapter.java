package com.buzzertech.bruz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prasad on 5/8/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryList> catergoryLists;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryList> catergoryLists){
        this.context = context;
        this.catergoryLists = catergoryLists;
    }
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.catergory_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {
        holder.img.setImageResource(catergoryLists.get(position).getImage());
        holder.categoryName.setText(catergoryLists.get(position).getCategoryName());
        holder.categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int categoryName = catergoryLists.get(position).getCategoryName();
                Intent intent = new Intent(context, CommonCategoryView.class);
                intent.putExtra("categoryName", categoryName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return catergoryLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView categoryName;
        RelativeLayout categories;
        public ViewHolder(View view){
            super(view);
            categories = (RelativeLayout) view.findViewById(R.id.categories);
            img = (ImageView) view.findViewById(R.id.cat_img);
            categoryName = (TextView) view.findViewById(R.id.cat_text);

        }
    }
}
