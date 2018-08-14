package com.buzzertech.bruz;



public class WallpapersList {

    private String imageUrl;
    private String thumb;
    private int ID;
    private String source;
    private String designer;
    private String designerProfile;

    public WallpapersList(String imageUrl, String thumb, int ID, String source, String designer, String designerProfile) {
        this.imageUrl = imageUrl;
        this.thumb = thumb;
        this.ID = ID;
        this.source = source;
        this.designer = designer;
        this.designerProfile = designerProfile;
    }

    public String getThumb() {
        return thumb;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public int getID(){return ID;}

    public String getSource(){return source;}

    public String getDesigner(){return designer;}

    public String getDesignerProfile(){return designerProfile;}

}
