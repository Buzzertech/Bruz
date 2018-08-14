package com.buzzertech.bruz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.ZoomListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;
import java.util.Objects;

/**
 * Created by Prasad on 3/28/2017.
 */
/*
public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {
    private List<WallpapersList> galleryList;
    private List<CarouselList> carouselLists;
    private Context context;

    private final static int HEADER_VIEW = 0;
    private final static int CONTENT_VIEW = 1;

  //  int[] sampleimages = {R.drawable.bruz, R.drawable.preview, R.drawable.ic_bruz};

    public WallpaperAdapter(Context context, List<WallpapersList> galleryList, List<CarouselList> carouselLists) {
        this.galleryList = galleryList;
        this.carouselLists = carouselLists;
        this.context = context;
    }

    @Override
    public WallpaperAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.wallpaperlistitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final WallpaperAdapter.ViewHolder viewholder, final int i) {
            viewholder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

                 /*      }
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

    }*/


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;

    List<CarouselList> carouselLists;
    List<WallpapersList> wallpapersLists;
    Context context;
    public WallpaperAdapter(Context context, List<CarouselList> carouselLists, List<WallpapersList> wallpapersLists) {
        this.context = context;
        this.carouselLists = carouselLists;
        this.wallpapersLists = wallpapersLists;
    }

    @Override
    public WallpaperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_layout, parent, false);
            return new ViewHolder(view, viewType);
        }
        else if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaperlistitem, parent, false);
            return new ViewHolder(view, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.ViewHolder holder, int position) {
        if(holder.view_type == 0){
            ViewHolder.carouselView.setPageCount(carouselLists.size());
            ViewHolder.carouselView.setImageListener(imageListener);
        }
        else if(holder.view_type == 1) {
            int lateralposition = position-1;

            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            TypedValue outValue = new TypedValue();
            //context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            //holder.linearLayout.setBackgroundResource(outValue.resourceId);
            holder.img.setClickable(true);
            if(Objects.equals(wallpapersLists.get(lateralposition).getThumb(), "")){
                Glide.with(context).load(wallpapersLists.get(lateralposition).getImageUrl()).override(216, 384).into(holder.img);
            }else {
                Glide.with(context).load(wallpapersLists.get(lateralposition).getThumb()).placeholder(R.drawable.preview).override(216, 384).crossFade().into(holder.img);
            }
            activityIntent(holder, lateralposition);
        }
    }

    private void activityIntent(final WallpaperAdapter.ViewHolder holder, final int position) {
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

    @Override
    public int getItemCount() {
        return wallpapersLists.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        switch(position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_ITEM;
        }
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        int view_type = 0;
        //Carousel
        static CarouselView carouselView;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType == TYPE_ITEM) {
                img = (ImageView) itemView.findViewById(R.id.img);

                view_type = 1;
            }
            else if(viewType == TYPE_HEADER){
            carouselView = (CarouselView) itemView.findViewById(R.id.carousel);
                view_type = 0;
            }

        }


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(final int position, final ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            int relLayoutWidth = ViewHolder.carouselView.getLayoutParams().width;
            int relLayoutHeight = ViewHolder.carouselView.getLayoutParams().height;
            Glide.with(context).load(carouselLists.get(position).getImageUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.preview).into(imageView);
            Glide.with(context).load(carouselLists.get(position).getImageUrl()).asBitmap().placeholder(R.drawable.preview).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>(SimpleTarget.SIZE_ORIGINAL, relLayoutHeight) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                    imageView.setBackground(drawable);
                }
            });
            imageView.isClickable();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string = carouselLists.get(position).getImageUrl();
                    int id = carouselLists.get(position).getID();
                    String Source = carouselLists.get(position).getSource();
                    String Designer = carouselLists.get(position).getDesigner();
                    String DesignersProfile = carouselLists.get(position).getDesignerProfile();
                    Intent i = new Intent(context, downloadActivity.class);
                    i.putExtra("url", string);
                    i.putExtra("id", id);
                    i.putExtra("source", Source);
                    i.putExtra("designer", Designer);
                    i.putExtra("designersprofile", DesignersProfile);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Home) context).toBundle();
                    //ActivityOptionsCompat options = ActivityOptionsCompat.
                    //      makeSceneTransitionAnimation((Home) context, (View) viewholder.img, "transition1");
                    context.startActivity(i, bundle);
                }
            });
        }
    };



}