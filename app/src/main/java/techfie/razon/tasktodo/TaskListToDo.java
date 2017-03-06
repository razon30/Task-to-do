package techfie.razon.tasktodo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.realm.Realm;

/**
 * Implementation of App Widget functionality.
 */
public class TaskListToDo extends AppWidgetProvider {



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int[] appWidgetIds) {

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        int todoSize = realm.where(ClassToDo.class).findAll().size();
        int remainTask = realm.where(ClassTask.class).equalTo("done","0").findAll().size();

        Intent intent = new Intent(context, TaskListForground.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.task_list_to_do);
        views.setTextViewText(R.id.todosize, todoSize+"");
        views.setOnClickPendingIntent(R.id.todosize, pendingIntent);
        views.setTextViewText(R.id.remainsize, remainTask+"");
        views.setOnClickPendingIntent(R.id.remainsize, pendingIntent);

        ComponentName component=new ComponentName(context,TaskListToDo.class);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.todosize);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.remainsize);
        appWidgetManager.updateAppWidget(component, views);

        // Instruct the widget manager to update the widget
       // appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    
}

