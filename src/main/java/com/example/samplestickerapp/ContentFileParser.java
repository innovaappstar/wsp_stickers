/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.example.samplestickerapp.entities.StickerContentGson;
import com.example.samplestickerapp.entities.StickerGson;
import com.example.samplestickerapp.entities.StickerPackGson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class ContentFileParser {

    private static final int LIMIT_EMOJI_COUNT = 3;

    @NonNull
    static List<StickerPack> parseStickerPacks(@NonNull InputStream contentsInputStream) throws IOException, IllegalStateException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(contentsInputStream))) {
            return readStickerPacks(reader);
        }
    }

    @NonNull
    static List<StickerPack> mergeBetweenStickerPack(@NonNull Context context,@NonNull List<StickerContentGson> alStickerContentGson){
        List<StickerPack> listStickerPack = new ArrayList<>();

        for (int i = 0; i < alStickerContentGson.size(); i++) {
            StickerContentGson stickerContentGson = alStickerContentGson.get(i);
            for (int j = 0; j < stickerContentGson.sticker_packs.size(); j++) {
                StickerPackGson stickerPackGson = stickerContentGson.sticker_packs.get(j);

                String identifier = stickerPackGson.identifier;
                String name = stickerPackGson.name;
                String publisher = stickerPackGson.publisher;
                String trayImageFile = stickerPackGson.tray_image_file;
                String publisherEmail = stickerPackGson.publisher_email;
                String publisherWebsite = stickerPackGson.publisher_website;
                String privacyPolicyWebsite = stickerPackGson.privacy_policy_website;
                String licenseAgreementWebsite = stickerPackGson.license_agreement_website;
                String imageDataVersion = stickerPackGson.image_data_version;
                boolean avoidCache = stickerPackGson.avoid_cache;
                List<Sticker> stickers = new ArrayList<>();

                String iosAppStoreLink = stickerContentGson.ios_app_store_link;
                for (int k = 0; k < stickerPackGson.stickers.size(); k++) {
                    StickerGson stickerGson = stickerPackGson.stickers.get(k);
                    String imageFileName = stickerGson.image_file;
                    ArrayList<String> emojis = stickerGson.emojis;
                    Sticker sticker = new Sticker(imageFileName ,emojis);

                    stickers.add(sticker);
                }

                String androidPlayStoreLink = stickerContentGson.android_play_store_link;
                boolean isWhitelisted = WhitelistCheck.isWhitelisted(context,stickerPackGson.identifier);

                StickerPack stickerPack = new StickerPack( identifier,  name,  publisher,  trayImageFile,  publisherEmail,  publisherWebsite,  privacyPolicyWebsite,  licenseAgreementWebsite,  imageDataVersion, avoidCache);
                stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
                stickerPack.setIosAppStoreLink(iosAppStoreLink);
                stickerPack.setIsWhitelisted(isWhitelisted);
                stickerPack.setStickers(stickers);

                listStickerPack.add(stickerPack);
            }
        }
        return listStickerPack;
    }

    @NonNull
    private static List<StickerPack> readStickerPacks(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        List<StickerPack> stickerPackList = new ArrayList<>();
        String androidPlayStoreLink = null;
        String iosAppStoreLink = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if ("android_play_store_link".equals(key)) {
                androidPlayStoreLink = reader.nextString();
            } else if ("ios_app_store_link".equals(key)) {
                iosAppStoreLink = reader.nextString();
            } else if ("sticker_packs".equals(key)) {
                reader.beginArray();
                while (reader.hasNext()) {
                    StickerPack stickerPack = readStickerPack(reader);
                    stickerPackList.add(stickerPack);
                }
                reader.endArray();
            } else {
                throw new IllegalStateException("unknown field in json: " + key);
            }
        }
        reader.endObject();
        if (stickerPackList.size() == 0) {
            throw new IllegalStateException("sticker pack list cannot be empty");
        }
        for (StickerPack stickerPack : stickerPackList) {
            stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
            stickerPack.setIosAppStoreLink(iosAppStoreLink);
        }
        return stickerPackList;
    }

    @NonNull
    private static StickerPack readStickerPack(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginObject();
        String identifier = null;
        String name = null;
        String publisher = null;
        String trayImageFile = null;
        String publisherEmail = null;
        String publisherWebsite = null;
        String privacyPolicyWebsite = null;
        String licenseAgreementWebsite = null;
        String imageDataVersion = "";
        boolean avoidCache = false;
        List<Sticker> stickerList = null;
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "identifier":
                    identifier = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "publisher":
                    publisher = reader.nextString();
                    break;
                case "tray_image_file":
                    trayImageFile = reader.nextString();
                    break;
                case "publisher_email":
                    publisherEmail = reader.nextString();
                    break;
                case "publisher_website":
                    publisherWebsite = reader.nextString();
                    break;
                case "privacy_policy_website":
                    privacyPolicyWebsite = reader.nextString();
                    break;
                case "license_agreement_website":
                    licenseAgreementWebsite = reader.nextString();
                    break;
                case "stickers":
                    stickerList = readStickers(reader);
                    break;
                case "image_data_version":
                    imageDataVersion = reader.nextString();
                    break;
                case "avoid_cache":
                    avoidCache = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalStateException("identifier cannot be empty");
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalStateException("name cannot be empty");
        }
        if (TextUtils.isEmpty(publisher)) {
            throw new IllegalStateException("publisher cannot be empty");
        }
        if (TextUtils.isEmpty(trayImageFile)) {
            throw new IllegalStateException("tray_image_file cannot be empty");
        }
        if (stickerList == null || stickerList.size() == 0) {
            throw new IllegalStateException("sticker list is empty");
        }
        if (identifier.contains("..") || identifier.contains("/")) {
            throw new IllegalStateException("identifier should not contain .. or / to prevent directory traversal");
        }
        if (TextUtils.isEmpty(imageDataVersion)) {
            throw new IllegalStateException("image_data_version should not be empty");
        }
        reader.endObject();
        final StickerPack stickerPack = new StickerPack(identifier, name, publisher, trayImageFile, publisherEmail, publisherWebsite, privacyPolicyWebsite, licenseAgreementWebsite, imageDataVersion, avoidCache);
        stickerPack.setStickers(stickerList);
        return stickerPack;
    }

    @NonNull
    private static List<Sticker> readStickers(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginArray();
        List<Sticker> stickerList = new ArrayList<>();

        while (reader.hasNext()) {
            reader.beginObject();
            String imageFile = null;
            List<String> emojis = new ArrayList<>(LIMIT_EMOJI_COUNT);
            while (reader.hasNext()) {
                final String key = reader.nextName();
                if ("image_file".equals(key)) {
                    imageFile = reader.nextString();
                } else if ("emojis".equals(key)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String emoji = reader.nextString();
                        emojis.add(emoji);
                    }
                    reader.endArray();
                } else {
                    throw new IllegalStateException("unknown field in json: " + key);
                }
            }
            reader.endObject();
            if (TextUtils.isEmpty(imageFile)) {
                throw new IllegalStateException("sticker image_file cannot be empty");
            }
            if (!imageFile.endsWith(".webp")) {
                throw new IllegalStateException("image file for stickers should be webp files, image file is: " + imageFile);
            }
            if (imageFile.contains("..") || imageFile.contains("/")) {
                throw new IllegalStateException("the file name should not contain .. or / to prevent directory traversal, image file is:" + imageFile);
            }
            stickerList.add(new Sticker(imageFile, emojis));
        }
        reader.endArray();
        return stickerList;
    }
}
