package com.example.samplestickerapp.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.samplestickerapp.StickerPack;
import com.example.samplestickerapp.StickerPackLoader;
import com.example.samplestickerapp.StickerPackValidator;
import com.example.samplestickerapp.WhitelistCheck;

import java.util.ArrayList;

public class LoadStickersListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
    private Context context;
    private IStickerResult iStickerResult;

    public interface IStickerResult{
        void onShowErrorMessage(String desResult);
        void onShowStickerPack(ArrayList<StickerPack> alStickerPack);
    }

    public LoadStickersListAsyncTask(Context context, IStickerResult iStickerResult) {
        this.context = context;
        this.iStickerResult = iStickerResult;
    }

    @Override
    protected Pair<String, ArrayList<StickerPack>> doInBackground(Void... voids) {
        ArrayList<StickerPack> stickerPackList;
        try {
                stickerPackList = StickerPackLoader.fetchStickerPacks(context);
                if (stickerPackList.size() == 0) {
                    return new Pair<>("could not find any packs", null);
                }
                for (StickerPack stickerPack : stickerPackList) {
                    StickerPackValidator.verifyStickerPackValidity(context, stickerPack);
                }
                return new Pair<>(null, stickerPackList);
        } catch (Exception e) {
            Log.e("EntryActivity", "error fetching sticker packs", e);
            return new Pair<>(e.getMessage(), null);
        }
    }

    @Override
    protected void onPostExecute(Pair<String, ArrayList<StickerPack>> stringListPair) {
            if (stringListPair.first != null) {
                iStickerResult.onShowErrorMessage(stringListPair.first);
            } else {
                iStickerResult.onShowStickerPack(stringListPair.second);
            }
    }
}