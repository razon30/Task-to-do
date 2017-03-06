package techfie.razon.tasktodo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;

/**
 * Created by razon30 on 16-12-16.
 */

public class ToDoReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        final String id = intent.getStringExtra("id");
        String info = intent.getStringExtra("note");

        if (realm.where(ClassToDo.class).equalTo("id",id).findFirst()!=null) {
            final ClassToDo task = realm.where(ClassToDo.class).equalTo("id", id).findFirst();

            Log.e("Serviceid", id);

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String[] formattedDate = {df.format(calendar.getTime())};

            final String[] time = {formattedDate[0]};
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // ClassToDo task = realm.where(ClassToDo.class).equalTo("id",id).findFirst();
                    task.setDone(true);
                    time[0] = task.getTime();
                }
            });

            Intent notificationIntent = new Intent(context, AlarmDialouge.class).putExtra("time", time[0])
                    .putExtra("note", info).putExtra("class", "1").putExtra("id", id);
            Intent notificationIntent1 = new Intent(context, TaskActivity.class).putExtra("time",
                    time[0])
                    .putExtra("note", info);
            PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(id), notificationIntent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);


            PugNotification.with(context)
                    .load()
                    .click(contentIntent)
                    .title("TaskToDo")
                    .message(info)
                    .bigTextStyle(info)
                    .smallIcon(R.drawable.logo)
                    .largeIcon(R.drawable.logo)
                    .flags(Notification.DEFAULT_ALL)
                    .color(R.color.colorPrimary)
                    .autoCancel(false)
                    .simple()
                    .build();

            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notificationIntent);
        }

    }
}