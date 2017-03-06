package techfie.razon.tasktodo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.ALARM_SERVICE;


public class FragmentRemain extends Fragment {

    RecyclerView recyclerView;
    AdapterRecycler adapter;
    LinearLayoutManager manager;
    RealmResults<ClassTask> list;
    ArrayList<ClassTask> taskList;
    TextView textView;
    Realm realm;

    FireworkyPullToRefreshLayout mPullToRefresh;
    private boolean mIsRefreshing;
    private static final int REFRESH_DELAY = 2000;

    int mYear, mMonth, mDay, mHour, mMinute;
    String format = "1";
    String dateAlarm = "0", timeAlarm = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_done, container, false);
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();

        mPullToRefresh = (FireworkyPullToRefreshLayout) view.findViewById(R.id.pullToRefresh);

        taskList = new ArrayList<ClassTask>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        textView = (TextView) view.findViewById(R.id.error);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        list = realm.where(ClassTask.class).equalTo("done", "0").findAll();
        if (list.size() > 0) {
            //   Collections.reverse(list);
            for (int i = list.size() - 1; i >= 0; i--) {
                taskList.add(list.get(i));
            }
            adapter = new AdapterRecycler(taskList, getActivity(), "1");
            recyclerView.setAdapter(adapter);
        } else if (list.isEmpty()) {
            mPullToRefresh.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        initRefreshView();

        mPullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefresh.setRefreshing(mIsRefreshing);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onCLick(View v, final int position) {

            }

            @Override
            public void onLongClick(View v, final int position) {

                final Context context = getActivity();

                LayoutInflater layoutInflater1 = LayoutInflater.from(context);
                View view = layoutInflater1.inflate(R.layout.layout_choose_item, null);

                TextView edit = (TextView) view.findViewById(R.id.edit);
                TextView cancel = (TextView) view.findViewById(R.id.cancel);
                TextView delete = (TextView) view.findViewById(R.id.delete);


                final MaterialDialog dialog = new MaterialDialog(getActivity());
                dialog.setView(view);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = list.get(list.size() - (position + 1)).getId();
                        String note = list.get(position).getNote();
                        worksOnEdit(id, note, position);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = list.get(position).getId();
                        stopAlarm(id);
                        adapter.removeItem(position);
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        }));


        return view;
    }

    private void stopAlarm(String id) {

        Intent alarmIntent4 = new Intent(getActivity(), MyReceiver.class).putExtra("id", id);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getActivity(), Integer.parseInt
                        (id),
                alarmIntent4, 0);
        AlarmManager manager4 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager4.cancel(pendingIntent4);


    }

    private void worksOnEdit(final String id, final String note, final int position) {

        final Context context = getActivity();

        LayoutInflater layoutInflater1 = LayoutInflater.from(context);
        View view = layoutInflater1.inflate(R.layout.layout_edit_alarm, null);
        final Button txtDate, txtTime;
        txtDate = (Button) view.findViewById(R.id.date);
        txtTime = (Button) view.findViewById(R.id.time);
        Button setAlarm = (Button) view.findViewById(R.id.ok1);
        TextView cancel = (TextView) view.findViewById(R.id.cancel1);
        final EditText input_text = (EditText) view.findViewById(R.id.input_edit);


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

                doAlarm(position, dateAlarm, timeAlarm, input_text, note, id, format, mYear, mMonth, mDay, mHour, mMinute, timeAlarm);
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

    private void doAlarm(final int position, String dateAlarm, String alarm, final EditText input_text, final String note, final String id, String format, int mYear, int mMonth, int
            mDay, int mHour, int mMinute, String timeAlarm) {

        final Context context = getActivity();

        if (dateAlarm.equals("0") && timeAlarm.equals("0")) {

            ClassTask classTask = realm.where(ClassTask.class).equalTo("id",id).findFirst();
            String note2 = classTask.getNote();
            final String formattedDate = classTask.getTime();
            final String time = classTask.getTimeafter();
            final String recordPath = classTask.getAudioPath();

            if (!input_text.getText().toString().isEmpty()){
                note2 = input_text.getText().toString();
            }

            final String finalNote = note2;
            adapter.removeItem(position);
            ClassTask task = new ClassTask(finalNote, formattedDate, String.valueOf(time), String
                    .valueOf(id),
                    "0", recordPath);
            adapter.addItem(position,task);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                  //  realm.where(ClassTask.class).equalTo("id",id).findFirst().deleteFromRealm();
//                    ClassTask task = new ClassTask(finalNote, formattedDate, String.valueOf(time), String
//                            .valueOf(id),
//                            "0", recordPath);
//                    ClassTask task1 = realm.copyToRealm(task);
//                    adapter.notifyItemChanged(position);
//                    adapter.notifyDataSetChanged();
////
////
////                    ClassTask classToDo = realm.where(ClassTask.class).equalTo("id", id).findFirst();
////                    if (!input_text.getText().toString().isEmpty()) {
////                        classToDo.setNote(input_text.getText().toString());
////                        adapter.notifyDataSetChanged();
////                    }
//                    //  classToDo.setAlarmSet(true);
//
//                }
//            });
            return;
        }

        if (!dateAlarm.equals("0") && !timeAlarm.equals("0")) {
            String[] times = timeAlarm.split(":");
            final int hour = Integer.parseInt(times[0]);

            SimpleDateFormat df = new SimpleDateFormat("E, hh:mm:ss aa, dd-MMM-yyyy");
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


            Calendar calendar1 = Calendar.getInstance();
            final String formattedDate = df.format(calendar1.getTime());

            ClassTask classTask = realm.where(ClassTask.class).equalTo("id", id).findFirst();
            String note1 = classTask.getNote().toString();
            final String recordPath = classTask.getAudioPath().toString();

            // classToDo.setTime(time);
            if (!input_text.getText().toString().isEmpty()) {
                note1 = input_text.getText().toString();
                //  classToDo.setNote(input_text.getText().toString());

            }

            final String note2 = note1;
            stopAlarm(id);



            adapter.removeItem(position);
            ClassTask task = new ClassTask(note2, formattedDate, String.valueOf(time), String
                    .valueOf(id),
                    "0", recordPath);
            adapter.addItem(position,task);

            Intent myIntent = new Intent(context, MyReceiver.class).putExtra("id", id + "").putExtra
                    ("note", note2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(id), myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                 //   realm.where(ClassTask.class).equalTo("id", id).findFirst().deleteFromRealm();
//
//                    ClassTask task1 = realm.copyToRealm(task);
//                    adapter.notifyItemChanged(position);
//                    adapter.notifyDataSetChanged();
//                    //  classToDo.setAlarmSet(true);
//
//                }
//            });

            return;

        } else if (dateAlarm.equals("0") && !timeAlarm.equals("0")) {
            Toast.makeText(getActivity(), "Please Set a Date", Toast.LENGTH_LONG).show();
            return;
        } else if (!dateAlarm.equals("0") && timeAlarm.equals("0")) {
            Toast.makeText(getActivity(), "Please set a Time", Toast.LENGTH_LONG).show();
            return;
        }

    }


    private void initRefreshView() {
        mPullToRefresh.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        list = realm.where(ClassTask.class).equalTo("done", "0").findAll();
                        if (list.size() > 0) {

                            taskList.clear();
                            //   Collections.reverse(list);
                            for (int i = list.size() - 1; i >= 0; i--) {
                                taskList.add(list.get(i));
                            }
                            adapter = new AdapterRecycler(taskList, getActivity(), "1");
                            recyclerView.setAdapter(adapter);
                        } else if (list.isEmpty()) {
                            mPullToRefresh.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }

                        mPullToRefresh.setRefreshing(mIsRefreshing = false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }


    public FragmentRemain() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface ClickListener {

        void onCLick(View v, int position);

        void onLongClick(View v, int position);

    }

    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child));
                    }

                }
            });


        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onCLick(child, rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
}
