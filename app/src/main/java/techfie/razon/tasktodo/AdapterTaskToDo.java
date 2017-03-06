package techfie.razon.tasktodo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by razon30 on 07-12-16.
 */

public class AdapterTaskToDo extends RecyclerView.Adapter<AdapterTaskToDo.ViewHolderAdapterTaskToDo> {

    ArrayList<ClassToDo> activityList;
    Context context;
    private LayoutInflater layoutInflater;

    int mYear, mMonth, mDay, mHour, mMinute;
    String format = "1";
    String dateAlarm = "0", timeAlarm = "0";
    Realm realm;
    private boolean onBind;

    int pos;
    String id1;


    public AdapterTaskToDo(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
      //  this.activityList = activityList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<ClassToDo> activityList) {
      //  Collections.reverse(activityList);
      //  this.activityList.clear();
        this.activityList = activityList;
     //   notifyDataSetChanged();
    }


    @Override
    public ViewHolderAdapterTaskToDo onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_todo_list, parent, false);
        ViewHolderAdapterTaskToDo viewHolder = new ViewHolderAdapterTaskToDo(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderAdapterTaskToDo holder, final int position) {

      //  pos = position;
        ClassToDo currentToDo = activityList.get(position);


      //  id1 = id;
        final boolean isDone = currentToDo.isDone();


        if (!isDone) {
            final String note = currentToDo.getNote();
            String time = currentToDo.getTime();
            final String id = currentToDo.getId();
            final boolean isAlarmSet = currentToDo.isAlarmSet();

            holder.note.setText(note);

            if (time.equals("1")) {
                holder.alarmTime.setVisibility(View.GONE);
            } else {
                holder.alarmTime.setText(time);
            }

            if (isAlarmSet) {
                holder.isAlarmSet.setBackgroundResource(R.drawable.alarm_on);
            }

            holder.isAlarmSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCustomTimeDate(holder, note, id);
                }
            });
            holder.isDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, activityList.size());
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(ClassToDo.class).equalTo("id", id).findFirst().deleteFromRealm();
                           if (isAlarmSet) {
                               stopAlarm(id);
                           }
                        }
                    });
                    notifyDataSetChanged();
                }
            });

//            activityList.remove(position);
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                  // activityList.remove(position);
//                    realm.where(ClassToDo.class).equalTo("id", id).findFirst().deleteFromRealm();
//                }
//            });
//            notifyDataSetChanged();
        }



//        if (holder.isDone.isChecked()) {
//            activityList.remove(position);
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.where(ClassToDo.class).equalTo("id", id).findFirst().deleteFromRealm();
//                }
//            });
//            notifyItemRemoved(position);
//        }

//        holder.isDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activityList.remove(position);
////                onBind = true;
//                holder.isDone.setChecked(true);
//              //  onBind = false;
//              //  notifyDataSetChanged();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.where(ClassToDo.class).equalTo("id", id).findFirst().deleteFromRealm();
//                    }
//                });
//                notifyDataSetChanged();
//            }
//        });


    }

    private void stopAlarm(String id) {

        Intent alarmIntent4 = new Intent(context, ToDoReceiver.class).putExtra("id", id);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, Integer.parseInt
                        (id),
                alarmIntent4, 0);
        AlarmManager manager4 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager4.cancel(pendingIntent4);


    }


    private void setCustomTimeDate(final ViewHolderAdapterTaskToDo holder, final String note, final String id) {

        LayoutInflater layoutInflater1 = LayoutInflater.from(context);
        View view = layoutInflater1.inflate(R.layout.layout_time_date, null);
        final Button txtDate, txtTime;
        txtDate = (Button) view.findViewById(R.id.date);
        txtTime = (Button) view.findViewById(R.id.time);
        Button setAlarm = (Button) view.findViewById(R.id.ok1);
        TextView cancel = (TextView) view.findViewById(R.id.cancel1);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateAlarm = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                txtDate.setText(dateAlarm);

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                if (hourOfDay == 0) {

                                    hourOfDay += 12;
                                    format = "AM";

                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;
                                    format = "PM";

                                } else {

                                    format = "AM";
                                }
                                mHour = hourOfDay;
                                mMinute = minute;
                                timeAlarm = mHour + ":" + mMinute + ":" + format;
                                txtTime.setText(timeAlarm);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setView(view).show();

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateAlarm.equals("0")) {
                    Toast.makeText(context, "Please Choose a Date", Toast
                            .LENGTH_LONG).show();
                    return;
                }
                if (timeAlarm.equals("0")) {
                    Toast.makeText(context, "Please Choose A Time", Toast
                            .LENGTH_LONG).show();
                    return;
                }
                doAlarm(holder, note, id, format, mYear, mMonth, mDay, mHour, mMinute, timeAlarm);
                mMaterialDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMaterialDialog.dismiss();
            }
        });

    }

    private void doAlarm(ViewHolderAdapterTaskToDo holder, final String note, final String id, String format, int mYear, int mMonth, int
            mDay, int mHour, int mMinute, String timeAlarm) {


        String[] times = timeAlarm.split(":");
        final int hour = Integer.parseInt(times[0]);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, hh:mma");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, 0);
        if (format.equals("AM")) {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        } else if (format.equals("PM")) {
            calendar.set(Calendar.AM_PM, Calendar.PM);
        }
        final String time = df.format(calendar.getTime());

        Intent myIntent = new Intent(context, ToDoReceiver.class).putExtra("id", id + "").putExtra
                ("note", note);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(id), myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassToDo classToDo = realm.where(ClassToDo.class).equalTo("id", id).findFirst();
                classToDo.setTime(time);
                classToDo.setAlarmSet(true);
            }
        });

        holder.isAlarmSet.setBackgroundResource(R.drawable.alarm_on);
        holder.alarmTime.setText(time);
        holder.alarmTime.setVisibility(View.VISIBLE);

        Toast.makeText(context, "Alarm is set", Toast.LENGTH_LONG).show();

    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }



    public class ViewHolderAdapterTaskToDo extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView note;
        Button isDone;
        Button isAlarmSet;
        TextView alarmTime;

        public ViewHolderAdapterTaskToDo(View itemView) {
            super(itemView);

            note = (TextView) itemView.findViewById(R.id.note);
            isDone = (Button) itemView.findViewById(R.id.isDone);
            isAlarmSet = (Button) itemView.findViewById(R.id.alarmIndicator);
            alarmTime = (TextView) itemView.findViewById(R.id.alarmTime);
        }

        @Override
        public void onClick(View view) {

        }
    }
}