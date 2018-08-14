package com.buzzertech.bruz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.ZoomListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

/*
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private List<CategoryViewList> wallpapersLists;
    private Context context;

    public CommonAdapter(Context context, List<CategoryViewList> wallpapersLists){
        this.context = context;
        this.wallpapersLists = wallpapersLists;
    }
    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catwallpaperlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonAdapter.ViewHolder holder, final int position) {
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(wallpapersLists.get(position).getImageUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.preview).into(holder.img);
        activityIntent(holder, position);

    }

    private void activityIntent(final CommonAdapter.ViewHolder holder, final int position) {
        Zoomy.Builder builder = new Zoomy.Builder((Activity) context).target(holder.img)
                .interpolator(new AccelerateDecelerateInterpolator())
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        String string = wallpapersLists.get(position).getImageUrl();
                        int id = wallpapersLists.get(position).getID();
                        String Source = wallpapersLists.get(position).getSource();
                        String Designer = wallpapersLists.get(position).getDesigner();
                        String DesignersProfile = wallpapersLists.get(position).getDesignerProfile();
                        Intent i = new Intent(context, downloadActivity.class);
                        i.putExtra("url", string);
                        i.putExtra("id", id);
                        i.putExtra("source", Source);
                        i.putExtra("designer", Designer);
                        i.putExtra("designersprofile", DesignersProfile);
                        holder.img.setTransitionName("transition1");
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Home) context).toBundle();
                        //ActivityOptionsCompat options = ActivityOptionsCompat.
                        //      makeSceneTransitionAnimation((Home) context, (View) viewholder.img, "transition1");
                        context.startActivity(i, bundle);

                        /*
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = galleryList.get(i).getImageUrl();
                                int id = galleryList.get(i).getID();
                                String Source = galleryList.get(i).getSource();
                                String Designer = galleryList.get(i).getDesigner();
                                String DesignersProfile = galleryList.get(i).getDesignerProfile();
                                Intent i = new Intent(context, downloadActivity.class);
                                i.putExtra("url", string);
                                i.putExtra("id", id);
                                i.putExtra("source", Source);
                                i.putExtra("designer", Designer);
                                i.putExtra("designersprofile", DesignersProfile);
                                viewholder.img.setTransitionName("transition1");
                                Slide slide = new Slide();
                                slide.setDuration(1000);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Home) context).toBundle();
                                //ActivityOptionsCompat options = ActivityOptionsCompat.
                                //      makeSceneTransitionAnimation((Home) context, (View) viewholder.img, "transition1");
                                context.startActivity(i, bundle);

                            }
                        });*/
/*
                    }
                })
                .zoomListener(new ZoomListener() {
                    @Override
                    public void onViewStartedZooming(View view) {
                        //View started zooming
                    }

                    @Override
                    public void onViewEndedZooming(View view) {
                        //View ended zooming
                    }
                });
        builder.register();
    }
/*
    @Override
    public int getItemCount() {
        return wallpapersLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View view){
            super(view);
            img = (ImageView) view.findViewById(R.id.img);


        }
    }
}
*/
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    private List<CategoryViewList> galleryList;
    private Context context;


    //  int[] sampleimages = {R.drawable.bruz, R.drawable.preview, R.drawable.ic_bruz};

    public CommonAdapter(Context context, List<CategoryViewList> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catwallpaperlist, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CommonAdapter.ViewHolder viewholder, final int i) {
        viewholder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!Objects.equals(galleryList.get(i).getThumb(), ""))
            Glide.with(context).load(galleryList.get(i).getThumb()).placeholder(R.drawable.preview).override(216, 384).into(viewholder.img);
        Glide.with(context).load(galleryList.get(i).getImageUrl()).placeholder(R.drawable.preview).override(216, 384).into(viewholder.img);
        Zoomy.Builder builder = new Zoomy.Builder((Activity) context).target(viewholder.img)
                .interpolator(new AccelerateDecelerateInterpolator())
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        String string = galleryList.get(i).getImageUrl();
                        int id = galleryList.get(i).getID();
                        String Source = galleryList.get(i).getSource();
                        String Designer = galleryList.get(i).getDesigner();
                        String DesignersProfile = galleryList.get(i).getDesignerProfile();
                        Intent i = new Intent(context, downloadActivity.class);
                        i.putExtra("url", string);
                        i.putExtra("id", id);
                        i.putExtra("source", Source);
                        i.putExtra("designer", Designer);
                        i.putExtra("designersprofile", DesignersProfile);
                        viewholder.img.setTransitionName("transition1");
                        Slide slide = new Slide();
                        slide.setDuration(1000);
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((CommonCategoryView) context).toBundle();
                        //ActivityOptionsCompat options = ActivityOptionsCompat.
                        //      makeSceneTransitionAnimation((Home) context, (View) viewholder.img, "transition1");
                        context.startActivity(i, bundle);

                        /*
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = galleryList.get(i).getImageUrl();
                                int id = galleryList.get(i).getID();
                                String Source = galleryList.get(i).getSource();
                                String Designer = galleryList.get(i).getDesigner();
                                String DesignersProfile = galleryList.get(i).getDesignerProfile();
                                Intent i = new Intent(context, downloadActivity.class);
                                i.putExtra("url", string);
                                i.putExtra("id", id);
                                i.putExtra("source", Source);
                                i.putExtra("designer", Designer);
                                i.putExtra("designersprofile", DesignersProfile);
                                viewholder.img.setTransitionName("transition1");
                                Slide slide = new Slide();
                                slide.setDuration(1000);
                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Home) context).toBundle();
                                //ActivityOptionsCompat options = ActivityOptionsCompat.
                                //      makeSceneTransitionAnimation((Home) context, (View) viewholder.img, "transition1");
                                context.startActivity(i, bundle);

                            }
                        });*/

                      }}
                    )
                    .zoomListener(new ZoomListener() {
                        @Override
                        public void onViewStartedZooming(View view) {
                            //View started zooming
                        }

                        @Override
                        public void onViewEndedZooming(View view) {
                            //View ended zooming
                        }
                    });
            builder.register();
    }




    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
       // private CarouselView carouselView;

        ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
           // carouselView = (CarouselView) view.findViewById(R.id.carousel);
        }
    }

    }
