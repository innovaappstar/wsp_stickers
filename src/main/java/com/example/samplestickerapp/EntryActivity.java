/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.snatik.storage.Storage;
import com.snatik.storage.helpers.OrderType;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryActivity extends BaseActivity {
    private View progressBar;
    private LoadListAsyncTask loadListAsyncTask;
    Storage storage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new Storage(getApplicationContext());

        setContentView(R.layout.activity_entry);
        overridePendingTransition(0, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // todo
        if(checkAndRequestWriteExternalStoragePermissions(this)){
            showFiles(storage.getExternalStorageDirectory());
        }else{

        }
        readFiles(EntryActivity.this, storage);

        progressBar = findViewById(R.id.entry_activity_progress);
        loadListAsyncTask = new LoadListAsyncTask(this);
        loadListAsyncTask.execute();

    }

    private final static int REQUEST_ID_WRITE_EXTERNAL_STORAGE_PERMISSION = 24;
    public static boolean checkAndRequestWriteExternalStoragePermissions(Activity activity) {
        int modifyAudioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (modifyAudioPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ID_WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        readFiles(EntryActivity.this, storage);
                    }
                }
                break;
            }
        }
    }

    private static int WRITE_PERMISSION = 1;
    private static int READ_PERMISSION = 2;

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }

    /**@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            write();
        } else if (requestCode == READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            read();
        }
    }**/

    /**
     * leeremos los archivos para luego suplantar el repositorio de los assets
     * @param context
     */
    private void readFiles(Context context, Storage storage){
        boolean isWritable = storage.isExternalWritable();
        // get external storage
        String path = storage.getExternalStorageDirectory();
        List<File> listFiles = storage.getFiles(path);

        String pathAssets = path + File.separator + "assets/";
        List<File> filesAssets = storage.getFiles(pathAssets);
        String path3 = storage.getExternalStorageDirectory();
        List<File> files2 = storage.getNestedFiles(path3);

        List<File> files3 = storage.getFiles(storage.getExternalStorageDirectory());
        if (files3 != null) {
            Collections.sort(files3, OrderType.NAME.getComparator());
        }

        // new dir
        String newDir = path + File.separator + "My Sample Directory";
        storage.createDirectory(newDir);

        try {
// new dir
            String path2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            path2 = "/storage/emulated/0/Android/data/";
            Log.d("Files", "Path: " + path2);
            File f = new File(path2);
            File file[] = f.listFiles();
            Log.d("Files", "Size: "+ file.length);
            for (int i=0; i < file.length; i++)
            {
                Log.d("Files", "FileName:" + file[i].getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showFiles(String path) {
//        mPathView.setText(path);
        List<File> files = storage.getFiles(path);
        if (files != null) {
            Collections.sort(files, OrderType.NAME.getComparator());
        }
//        mFilesAdapter.setFiles(files);
//        mFilesAdapter.notifyDataSetChanged();
    }

    private void showStickerPack(ArrayList<StickerPack> stickerPackList) {
        progressBar.setVisibility(View.GONE);
        if (stickerPackList.size() > 1) {
            final Intent intent = new Intent(this, StickerPackListActivity.class);
            intent.putParcelableArrayListExtra(StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA, stickerPackList);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            final Intent intent = new Intent(this, StickerPackDetailsActivity.class);
            intent.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, false);
            intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, stickerPackList.get(0));
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void showErrorMessage(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Log.e("EntryActivity", "error fetching sticker packs, " + errorMessage);
        final TextView errorMessageTV = findViewById(R.id.error_message);
        errorMessageTV.setText(getString(R.string.error_message, errorMessage));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadListAsyncTask != null && !loadListAsyncTask.isCancelled()) {
            loadListAsyncTask.cancel(true);
        }
    }

    static class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<EntryActivity> contextWeakReference;

        LoadListAsyncTask(EntryActivity activity) {
            this.contextWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Pair<String, ArrayList<StickerPack>> doInBackground(Void... voids) {
            ArrayList<StickerPack> stickerPackList;
            try {
                final Context context = contextWeakReference.get();
                if (context != null) {
                    stickerPackList = StickerPackLoader.fetchStickerPacks(context);
                    if (stickerPackList.size() == 0) {
                        return new Pair<>("could not find any packs", null);
                    }
                    for (StickerPack stickerPack : stickerPackList) {
                        StickerPackValidator.verifyStickerPackValidity(context, stickerPack);
                    }
                    return new Pair<>(null, stickerPackList);
                } else {
                    return new Pair<>("could not fetch sticker packs", null);
                }
            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<>(e.getMessage(), null);
            }
        }

        @Override
        protected void onPostExecute(Pair<String, ArrayList<StickerPack>> stringListPair) {

            final EntryActivity entryActivity = contextWeakReference.get();
            if (entryActivity != null) {
                if (stringListPair.first != null) {
                    entryActivity.showErrorMessage(stringListPair.first);
                } else {
                    entryActivity.showStickerPack(stringListPair.second);
                }
            }
        }
    }
}
