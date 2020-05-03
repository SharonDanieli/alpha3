package com.example.alpha3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

public class BatteryLevelReceiver extends BroadcastReceiver {
    /*int level;
    int scale;
     */
    private boolean highmsgFlag, lowmsgFlag;
    int highlevel, lowlevel;
    String st;

    public BatteryLevelReceiver() {
        lowmsgFlag = false;
        highmsgFlag = false;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        /*int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level < 80) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View dialogView = layoutInflater.inflate(R.layout.dialog_end_set, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            builder.setMessage("Are you sure you want to end the set?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

         */
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        highlevel = settings.getInt("sethighLevel", 60);
        lowlevel = settings.getInt("setlowLevel", 25);

        int batLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (batStatus != BatteryManager.BATTERY_STATUS_CHARGING) {
            if (batLevel <= highlevel) {
                if (!highmsgFlag) {
                    highmsgFlag = true;
                    st = "Low battery level: " + batLevel + "%\nPlease charge !";

                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context.getApplicationContext(), "my_notify");
                    Intent tmpInt = new Intent(context.getApplicationContext(), BatteryLevelReceiver.class);
                    PendingIntent pI = PendingIntent.getActivity(context, 0, tmpInt, 0);
                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

                    bigText.setBigContentTitle("Low battery alarm!");
                    bigText.setSummaryText("Battery level is "+batLevel+"%");
                    myNoti.setContentIntent(pI);
                    myNoti.setContentTitle("Low battery alarm!");
                    myNoti.setContentText(st);
                    myNoti.setPriority(Notification.PRIORITY_MAX);
                    myNoti.setStyle(bigText);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        String channelId = "my_notify";
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_HIGH);
                        nm.createNotificationChannel(channel);
                        myNoti.setChannelId(channelId);
                    }
                    nm.notify(0, myNoti.build());

                    Toast toast=Toast. makeText(context,st,Toast. LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast. show();

                } else if (batLevel <= lowlevel) {
                    if (!lowmsgFlag) {
                        lowmsgFlag=true;
                        st="Low battery level: " + batLevel + "%\nPlease charge now !!!";

                        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder myNoti = new NotificationCompat.Builder(context.getApplicationContext(), "my_notify");
                        Intent tmpInt = new Intent(context.getApplicationContext(), BatteryLevelReceiver.class);
                        PendingIntent pI = PendingIntent.getActivity(context, 0, tmpInt, 0);
                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

                        bigText.setBigContentTitle("Low battery alarm !!!");
                        bigText.setSummaryText("Battery level is "+batLevel+"%");
                        myNoti.setContentIntent(pI);
                        myNoti.setContentTitle("Low battery alarm !!!");
                        myNoti.setContentText(st);
                        myNoti.setPriority(Notification.PRIORITY_MAX);
                        myNoti.setStyle(bigText);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {
                            String channelId = "my_notify";
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_HIGH);
                            nm.createNotificationChannel(channel);
                            myNoti.setChannelId(channelId);
                        }
                        nm.notify(0, myNoti.build());
                        Toast toast=Toast. makeText(context,st,Toast. LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast. show();
                    }
                }
            }
        } else if (highmsgFlag || lowmsgFlag) { // only if charging
            highmsgFlag = false;
            lowmsgFlag = false;
        }
    }
}
