package com.buzzertech.bruz;

/**
 * Created by Prasad on 5/8/2017.
 */

public class CategoryList {
    private int image;
    private int categoryName;

    public CategoryList(){
    }

    public CategoryList(int image, int categoryName){
        this.image = image;
        this.categoryName = categoryName;
    }

    public void setCategoryName(int name){
        this.categoryName = name;
    }

    public int getCategoryName(){
        return categoryName;
    }

    public void setImage(int resource){
        this.image = resource;
    }

    public int getImage(){
        return image;
    }



}
