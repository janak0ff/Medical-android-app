package human.anatomy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;


public class WebViewApp extends Application {
    private static final String ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID;

    static  Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

        setupActivityListener();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        if (Config.PUSH_ENABLED) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            OneSignal.initWithContext(this);
            OneSignal.setAppId(ONESIGNAL_APP_ID);

            OneSignal.setNotificationOpenedHandler(
                    new OneSignal.OSNotificationOpenedHandler() {
                        @Override
                        public void notificationOpened(OSNotificationOpenedResult result) {
                            // Capture Launch URL (App URL) here

                            JSONObject data = result.getNotification().getAdditionalData();
                            String launchUrl = result.getNotification().getLaunchURL();
                            String url = null;
                            if (data!=null && data.has("url"))
                            {
                                try {
                                    url = data.getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("openURL", launchUrl);
                            intent.putExtra("ONESIGNAL_URL", url);

                            Log.i("OneSignalExample", "openURL = " + launchUrl);
                            startActivity(intent);
                        }
                    });
        }


        if ((Config.SHOW_BANNER_AD) || (Config.SHOW_FULL_SCREEN_AD)) {
            if (Config.USE_FACEBOOK_ADS) {
                AudienceNetworkAds.initialize(this);
                AdSettings.addTestDevice("bf26e52d-43b9-4814-99ee-2b82136d7077");
            } else {
                MobileAds.initialize(this, null);
            }
        }
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (Config.PREVENT_SCREEN_CAPTURE) {
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}