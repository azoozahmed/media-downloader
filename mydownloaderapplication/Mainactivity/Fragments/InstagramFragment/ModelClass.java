package com.example.mydownloaderapplication.Mainactivity.Fragments.InstagramFragment;

public class ModelClass {
    String video_url,display_url;

    public ModelClass(String video_url, String display_url) {
        this.video_url = video_url;
        this.display_url = display_url;
    }
    public String getVideo_url() {
        return video_url;
    }
    public String getDisplay_url() {
        return display_url;
    }

}
