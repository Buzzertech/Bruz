package com.buzzertech.bruz;

/**
 * Created by Prasad on 5/4/2017.
 */

public class CarouselList {

    private String imageUrl;
    private int ID;
    private String source;
    private String designer;
    private String designerProfile;

    public CarouselList(String imageUrl, int ID, String source, String designer, String designerProfile) {
        this.imageUrl = imageUrl;
        this.ID = ID;
        this.source = source;
        this.designer = designer;
        this.designerProfile = designerProfile;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setHeader(String header){
        this.imageUrl = header;
    }
    public int getID(){return ID;}

    public String getSource(){return source;}

    public String getDesigner(){return designer;}

    public String getDesignerProfile(){return designerProfile;}

}
