package com.example.samplestickerapp.utils;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.samplestickerapp.BaseActivity;
import com.example.samplestickerapp.BuildConfig;
import com.example.samplestickerapp.R;
import com.example.samplestickerapp.WhitelistCheck;

public class StickerUtils {
//    private static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
//    private static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
//    private static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
//
//    private static final String CONSUMER_WHATSAPP_PACKAGE_NAME = "com.whatsapp";
//    private static final String SMB_WHATSAPP_PACKAGE_NAME = "com.whatsapp.w4b";
//
//    private static final int ADD_PACK = 200;
//    private static final String TAG = "AddStickerPackActivity";
//
//    protected static void addStickerPackToWhatsApp(Activity activity,String identifier, String stickerPackName) {
//        try {
//            //if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
//            if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(activity.getPackageManager()) && !WhitelistCheck.isWhatsAppSmbAppInstalled(activity.getPackageManager())) {
//                Toast.makeText(activity, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
//                return;
//            }
//            final boolean stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(activity, identifier);
//            final boolean stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(activity, identifier);
//            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
//                //ask users which app to add the pack to.
//                launchIntentToAddPackToChooser(activity,identifier, stickerPackName);
//            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
//                launchIntentToAddPackToSpecificPackage(activity,identifier, stickerPackName, CONSUMER_WHATSAPP_PACKAGE_NAME);
//            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
//                launchIntentToAddPackToSpecificPackage(activity,identifier, stickerPackName, SMB_WHATSAPP_PACKAGE_NAME);
//            } else {
//                Toast.makeText(activity, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "error adding sticker pack to WhatsApp", e);
//            Toast.makeText(activity, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
//    private static void launchIntentToAddPackToChooser(Activity activity, String identifier, String stickerPackName) {
//        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
//        try {
//            activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.add_to_whatsapp)), ADD_PACK);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(activity, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private static void launchIntentToAddPackToSpecificPackage(Activity activity,String identifier, String stickerPackName, String whatsappPackageName) {
//        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
//        intent.setPackage(whatsappPackageName);
//        try {
//            activity.startActivityForResult(intent, ADD_PACK);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(activity, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @NonNull
//    private static Intent createIntentToAddStickerPack(String identifier, String stickerPackName) {
//        Intent intent = new Intent();
//        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
//        intent.putExtra(EXTRA_STICKER_PACK_ID, identifier);
//        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
//        intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName);
//        return intent;
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ADD_PACK) {
//            if (resultCode == Activity.RESULT_CANCELED) {
//                if (data != null) {
//                    final String validationError = data.getStringExtra("validation_error");
//                    if (validationError != null) {
//                        if (BuildConfig.DEBUG) {
//                            //validation error should be shown to developer only, not users.
//                            BaseActivity.MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
//                        }
//                        Log.e(TAG, "Validation failed:" + validationError);
//                    }
//                } else {
//                    new StickerPackNotAddedMessageFragment().show(getSupportFragmentManager(), "sticker_pack_not_added");
//                }
//            }
//        }
//    }
}
