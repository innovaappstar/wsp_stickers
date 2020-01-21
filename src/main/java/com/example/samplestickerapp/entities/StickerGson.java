package com.example.samplestickerapp.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class StickerGson implements Serializable {
    public String image_file = "";
    public ArrayList<String> emojis = new ArrayList<>();
}
