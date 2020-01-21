package com.example.samplestickerapp.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class StickerContentGson implements Serializable {
    public String android_play_store_link = "";
    public String ios_app_store_link = "";
    public ArrayList<StickerPackGson> sticker_packs = new ArrayList<>();


}
