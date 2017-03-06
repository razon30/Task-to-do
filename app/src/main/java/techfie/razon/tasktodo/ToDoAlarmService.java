package techfie.razon.tasktodo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;

/**
 * Created by razon30 on 16-12-16.
 */

public class ToDoAlarmService extends IntentService
{

    private NotificationManager mManager;
    Uri notification;
    Ringtone r;

    public ToDoAlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        final String id = intent.getStringExtra("id");
        String info = intent.getStringExtra("note");

        Log.e("Serviceid",id);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String[] formattedDate = {df.format(calendar.getTime())};

        final String[] time = {formattedDate[0]};
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassTask task = realm.where(ClassTask.class).equalTo("id",id).findFirst();
                task.setDone("1");
                time[0] = task.getTime();
            }
        });

        Intent notificationIntent = new Intent(this, AlarmDialouge.class).putExtra("time",time[0]).putExtra
                ("note",info);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);


        PugNotification.with(this)
                .load()
                .click(contentIntent)
                .title("TaskToDo")
                .message(info)
                .bigTextStyle(info)
                .smallIcon(R.mipmap.ic_launcher)
                .largeIcon(R.mipmap.ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .color(R.color.colorPrimary)
                .autoCancel(false)
                .simple()
                .build();


    }


    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

}