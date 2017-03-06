package techfie.razon.tasktodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by razon30 on 16-12-16.
 */

public class PopUpReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
//        Realm.init(context);
//        Realm realm = Realm.getDefaultInstance();
//        final String id = intent.getStringExtra("id");
//        String info = intent.getStringExtra("note");
//
//        Log.e("Serviceid",id);
//
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        final String[] formattedDate = {df.format(calendar.getTime())};
//
//        final String[] time = {formattedDate[0]};
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                ClassToDo task = realm.where(ClassToDo.class).equalTo("id",id).findFirst();
//                task.setDone(true);
//                time[0] = task.getTime();
//            }
//        });

        Intent notificationIntent1 = new Intent(context, TaskListForground.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0, notificationIntent1,
                PendingIntent.FLAG_CANCEL_CURRENT);


//        PugNotification.with(context)
//                .load()
//                .click(contentIntent)
//                .title("TaskToDo")
//                .message("Your Task")
//                .bigTextStyle("See List of your task to do, task with alarm and completed task list")
//                .smallIcon(R.mipmap.ic_launcher)
//                .largeIcon(R.mipmap.ic_launcher)
//                .flags(Notification.FLAG_ONGOING_EVENT | Notification.FLAG_SHOW_LIGHTS)
//                .color(R.color.colorPrimary)
//                .autoCancel(false)
//                .simple()
//                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("TaskList")
                        .setContentText("See List of your task to do, task with alarm and completed task list")
                        .setContentIntent(contentIntent)
                        .setOngoing(true);

        Notification notification = mBuilder.getNotification();

       notificationManager.notify(0,notification);

//        Intent notificationIntent = new Intent(context, AlarmDialouge.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(notificationIntent);

    }
}