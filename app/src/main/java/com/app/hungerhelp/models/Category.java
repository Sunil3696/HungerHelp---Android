// Category.java
package com.app.hungerhelp.models;

public class Category {
    private String _id;
    private String title;
    private String icon;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return title;
    }
}


//package com.app.hungerhelp.models;
//
//public class Category {
//    private String _id;
//    private String title;
//    private String icon;
//
//    // Getters and Setters
//    public String getId() {
//        return _id;
//    }
//
//    public void setId(String _id) {
//        this._id = _id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//}
