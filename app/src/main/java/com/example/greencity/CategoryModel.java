package com.example.greencity;

public class CategoryModel {
    private String catlink;
    private String catname;

    public String getCatlink() {
        return catlink;
    }

    public void setCatlink(String catlink) {
        this.catlink = catlink;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public CategoryModel(String catlink, String catname) {
        this.catlink = catlink;
        this.catname = catname;
    }
}
