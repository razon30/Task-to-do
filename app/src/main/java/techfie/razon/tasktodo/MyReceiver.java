package techfie.razon.tasktodo;

/**
 * Created by razon30 on 14-11-16.
 */
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

public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        final String id = intent.getStringExtra("id");
        String info = intent.getStringExtra("note");

        if (realm.where(ClassTask.class).equalTo("id",id)!=null) {
          final ClassTask task = realm.where(ClassTask.class).equalTo("id", id).findFirst();

            String note2 = task.getNote().toString();
            Log.e("Serviceid", id);

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String[] formattedDate = {df.format(calendar.getTime())};

            final String[] time = {formattedDate[0]};
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    task.setDone("1");
                    time[0] = task.getTime();
                }
            });

            Intent notificationIntent = new Intent(context, AlarmDialouge.class).putExtra("time", time[0])
                    .putExtra("note", note2).putExtra("class", "0").putExtra("id", id);
            Intent notificationIntent1 = new Intent(context, TaskActivity.class).putExtra("time",
                    time[0])
                    .putExtra
                            ("note", note2);
            PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(id), notificationIntent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);


            PugNotification.with(context)
                    .load()
                    .click(contentIntent)
                    .title("TaskToDo")
                    .message(note2)
                    .bigTextStyle(note2)
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