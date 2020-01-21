package com.example.samplestickerapp.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StickerPackGson implements Serializable {
    public String identifier = "";
    public String name = "";
    public String publisher = "";
    public String tray_image_file = "";
    public String image_data_version = "";
    public boolean avoid_cache = false;
    public String publisher_email = "";
    public String publisher_website = "";
    public String privacy_policy_website = "";
    public String license_agreement_website = "";
    public List<StickerGson> stickers = new ArrayList<>();
}
