package techfie.razon.tasktodo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import me.drakeet.materialdialog.MaterialDialog;

import static android.content.Context.ALARM_SERVICE;


public class FragmentSetTask extends Fragment implements View.OnClickListener {

    Button fivMnt, tenMnt, fifteenMnt, twntyMnt, twintyFiveMnt, thirtyMnt, thirtyFIveMnt, fourtyMnt,
            fourtyFiveMnt, fiftyMnt, timeDate, timeAfter;
    //   Toolbar toolbar;
    EditText etNote;
    String note;
    SharedPreferences preferences;
    int mYear, mMonth, mDay, mHour, mMinute;
    String format = "1";
    String dateAlarm = "0", timeAlarm = "0";
    Realm realm;

    ImageView record_view;
    String recordPath = "1";
    int serial = 0;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_set_task, container, false);
        //   toolbar = (Toolbar)view. findViewById(R.id.toolbar);
        //   ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        initialization(view);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                worksOnOpeningDirectory();
            }
        }

        record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!hasPermissions(getActivity(), PERMISSIONS)) {
                        worksOnOpeningDirectory();
                    }else {
                        preferences = SampleApplication.preferences;
                        serial = preferences.getInt("serial", 1);
                        recordPath = worksOnRecord(serial);
                    }
                }else {
                    preferences = SampleApplication.preferences;
                    serial = preferences.getInt("serial", 1);
                    recordPath = worksOnRecord(serial);
                }
            }
        });


        fivMnt.setOnClickListener(this);
        tenMnt.setOnClickListener(this);
        fifteenMnt.setOnClickListener(this);
        twntyMnt.setOnClickListener(this);
        twintyFiveMnt.setOnClickListener(this);
        thirtyMnt.setOnClickListener(this);
        thirtyFIveMnt.setOnClickListener(this);
        fourtyMnt.setOnClickListener(this);
        fourtyFiveMnt.setOnClickListener(this);
        fiftyMnt.setOnClickListener(this);
//        fiftyFiveMnt.setOnClickListener(this);
//        sixtyMnt.setOnClickListener(this);
        timeDate.setOnClickListener(this);
        timeAfter.setOnClickListener(this);



        return view;
    }

    private void worksOnOpeningDirectory() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle("Request Permission");

        // set dialog message
        alertDialogBuilder
                .setMessage("You have to provide READ, Write and Record permission to enjoy the" +
                        " feature")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // request permission (see result in onRequestPermissionsResult() method)
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS,
                                PERMISSION_ALL);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "You are unable to record", Toast
                                .LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    private String worksOnRecord(int serial) {

        final String[] path = {"1"};



        File audioFile = getActivity().getCacheDir();
        path[0] = getActivity().getFilesDir() + "/" + "task" + serial + ".3gp";

        final MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(path[0]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e("development", "prepare() failed");
        }

        final MaterialDialog dialog = new MaterialDialog(getActivity());


        dialog.setTitle("Recording....");
        dialog.setPositiveButton("Done", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
                recorder.release();
                dialog.dismiss();

                Toast.makeText(getActivity(), "Record Completed", Toast.LENGTH_LONG).show();
            }
        });


        final String[] finalPath = {path[0]};
        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(finalPath[0]);
                boolean deleted = file.delete();
                finalPath[0] = "1";
                if (deleted) dialog.dismiss();

                Toast.makeText(getActivity(), "Audio Cancelled", Toast.LENGTH_LONG).show();


            }
        });

        dialog.show();


        return finalPath[0];
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1:

                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read) {
                    preferences = SampleApplication.preferences;
                    serial = preferences.getInt("serial", 1);
                    recordPath = worksOnRecord(serial);
                } else {
                    Toast.makeText(getActivity(), "You are unable to record", Toast
                            .LENGTH_LONG).show();
                }

                break;

        }
    }

    private void initialization(View view) {

        preferences = SampleApplication.preferences;
        etNote = (EditText) view.findViewById(R.id.edtNote);
        fivMnt = (Button) view.findViewById(R.id.fivemnt);
        tenMnt = (Button) view.findViewById(R.id.tenmnt);
        fifteenMnt = (Button) view.findViewById(R.id.fifteenmnt);
        twntyMnt = (Button) view.findViewById(R.id.twentymnt);
        twintyFiveMnt = (Button) view.findViewById(R.id.twintyfivemnt);
        thirtyMnt = (Button) view.findViewById(R.id.thirtymnt);
        thirtyFIveMnt = (Button) view.findViewById(R.id.thirtyfive);
        fourtyMnt = (Button) view.findViewById(R.id.fortymnt);
        fourtyFiveMnt = (Button) view.findViewById(R.id.fortyFiveMnt);
        fiftyMnt = (Button) view.findViewById(R.id.fiftyMnt);
//        fiftyFiveMnt = (Button)view.  findViewById(R.id.fiftyFiveMnt);
//        sixtyMnt = (Button)view.  findViewById(R.id.sixtyMnt);
        timeDate = (Button) view.findViewById(R.id.timeDate);
        timeAfter = (Button) view.findViewById(R.id.timeAfter);
        record_view = (ImageView) view.findViewById(R.id.record_task);

    }

    @Override
    public void onClick(View view) {

        long id = preferences.getLong("id", 0);

        Log.e("HomeId", id + "");

        note = etNote.getText().toString();
        etNote.setText("");
        if (note.isEmpty()) {
            note = "You Have Set An Alarm For a Task";
        }

        if (view.getId() == R.id.fivemnt) {
            setAlarm(note, 5, id);
        } else if (view.getId() == R.id.tenmnt) {
            setAlarm(note, 10, id);
        } else if (view.getId() == R.id.fifteenmnt) {
            setAlarm(note, 15, id);
        } else if (view.getId() == R.id.twentymnt) {
            setAlarm(note, 20, id);
        } else if (view.getId() == R.id.twintyfivemnt) {
            setAlarm(note, 25, id);
        } else if (view.getId() == R.id.thirtymnt) {
            setAlarm(note, 30, id);
        } else if (view.getId() == R.id.thirtyfive) {
            setAlarm(note, 35, id);
        } else if (view.getId() == R.id.fortymnt) {
            setAlarm(note, 40, id);
        } else if (view.getId() == R.id.fortyFiveMnt) {
            setAlarm(note, 45, id);
        } else if (view.getId() == R.id.fiftyMnt) {
            setAlarm(note, 50, id);
        }
//        else if (view.getId() == R.id.fiftyFiveMnt) {
//            setAlarm(note, 55, id);
//        } else if (view.getId() == R.id.sixtyMnt) {
//            setAlarm(note, 60, id);
//        }
        else if (view.getId() == R.id.timeDate) {
            setCustomTimeDate(note, id);
        } else if (view.getId() == R.id.timeAfter) {
            setCustomTimeAfter(note, id);
        }

    }

    private void setCustomTimeAfter(final String note, final long id) {

        final int[] value = {1};
        final int[] unit = {0};

        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_custom_time_after,
                null);
        final TextView tv = (TextView) view.findViewById(R.id.tv);
        NumberPicker np = (NumberPicker) view.findViewById(R.id.np);

        tv.setTextColor(Color.parseColor("#ffd32b3b"));
        np.setMinValue(1);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                tv.setText("Selected value : " + newVal);
                value[0] = newVal;
            }
        });
        final TextView tv1 = (TextView) view.findViewById(R.id.tv1);
        NumberPicker np1 = (NumberPicker) view.findViewById(R.id.np1);

        tv1.setTextColor(Color.parseColor("#FF2C834F"));
        final String[] values = {"Seconds", "Minutes", "Hours", "Days", "Weeks", "Months", "Years"};
        np1.setMinValue(0); //from array first value
        np1.setMaxValue(values.length - 1); //to array last value
        np1.setDisplayedValues(values);
        np1.setWrapSelectorWheel(true);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected value from picker
                tv1.setText("Selected Unit : " + values[newVal]);
                unit[0] = newVal;
            }
        });


        Button ok = (Button) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setView(view).show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMaterialDialog.dismiss();
                Toast.makeText(getActivity(), "Alarm is cancelled", Toast
                        .LENGTH_LONG).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = 1;//= value[0]*60;
                String un = "Minutes";
                if (unit[0] == 0) {
                    time = value[0];
                    un = value[0] + " Seconds";
                } else if (unit[0] == 1) {
                    time = value[0] * 60;
                    un = value[0] + " Minutes";
                } else if (unit[0] == 2) {
                    time = value[0] * 60 * 60;
                    un = value[0] + " Hours";
                } else if (unit[0] == 3) {
                    un = value[0] + " Day";
                    time = value[0] * 24 * 60 * 60;
                } else if (unit[0] == 4) {
                    time = value[0] * 7 * 24 * 60 * 60;
                    un = value[0] + " Weeks";
                } else if (unit[0] == 5) {
                    time = value[0] * 30 * 24 * 60 * 60;
                    un = value[0] + " Months";
                } else if (unit[0] == 6) {
                    time = value[0] * 365 * 24 * 60 * 60;
                    un = value[0] + " Years";
                }

                setCustomeTimeAfterAlarm(note, time, un, id);
                mMaterialDialog.dismiss();
            }
        });


    }

    private void setCustomeTimeAfterAlarm(final String note, final long time, final String un, final long id) {

      //  SharedPreferences.Editor editor = SampleApplication.preferences.edit();
        //editor.putInt()
        String format = "1";
        PendingIntent pendingIntent;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(calendar.getTime());

        Intent myIntent = new Intent(getActivity(), MyReceiver.class).putExtra("id", id + "").putExtra
                ("note", note);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis() + (time * 1000),
                pendingIntent);
        Log.e("HomeId", id + "");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassTask task = new ClassTask(note, formattedDate, un, String
                        .valueOf(id),
                        "0",recordPath);
                ClassTask task1 = realm.copyToRealm(task);
                recordPath = "1";
                //    new AdapterRecycler().addData(task);
            }
        });


        SharedPreferences.Editor editor = preferences.edit();
        long id1 = id + 1;
        int serial1 = serial+1;
        editor.putInt("serial",serial1);
        editor.putLong("id", id1);
        editor.commit();
        editor.apply();

        Toast.makeText(getActivity(), "Alarm is set", Toast
                .LENGTH_LONG).show();


    }

    private void setCustomTimeDate(final String note, final long id) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_time_date, null);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
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

        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setView(view).show();

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateAlarm.equals("0")) {
                    Toast.makeText(getActivity(), "Please Choose a Date", Toast
                            .LENGTH_LONG).show();
                    return;
                }
                if (timeAlarm.equals("0")) {
                    Toast.makeText(getActivity(), "Please Choose A Time", Toast
                            .LENGTH_LONG).show();
                    return;
                }
                doAlarm(note, id, format, mYear, mMonth, mDay, mHour, mMinute, timeAlarm);
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

    private void doAlarm(final String note, final long id, String format, int mYear, int mMonth, int
            mDay, int mHour, int mMinute, String timeAlarm) {


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

        Intent myIntent = new Intent(getActivity(), MyReceiver.class).putExtra("id", id + "").putExtra
                ("note", note);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        Calendar calendar1 = Calendar.getInstance();
        final String formattedDate = df.format(calendar1.getTime());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassTask task = new ClassTask(note, formattedDate, String.valueOf(time), String
                        .valueOf(id),
                        "0",recordPath);
                ClassTask task1 = realm.copyToRealm(task);
                recordPath = "1";
                //   new AdapterRecycler().addData(task);
            }
        });

        SharedPreferences.Editor editor = preferences.edit();
        long id1 = id + 1;
        int serial1 = serial+1;
        editor.putInt("serial",serial1);
        editor.putLong("id", id1);
        editor.commit();
        editor.apply();


        Toast.makeText(getActivity(), "Alarm is set", Toast
                .LENGTH_LONG).show();

    }

    private void setAlarm(final String note, final long time, final long id) {
        String format = "1";
        PendingIntent pendingIntent;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(calendar.getTime());

        Intent myIntent = new Intent(getActivity(), MyReceiver.class).putExtra("id", id + "").putExtra
                ("note", note);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), (int) id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis() + (time * 60 * 1000),
                pendingIntent);
        Log.e("HomeId", id + "");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassTask task = new ClassTask(note, formattedDate, String.valueOf(time) + " " +
                        "Minutes", String
                        .valueOf(id),
                        "0",recordPath);
                ClassTask classTask = realm.copyToRealm(task);
                recordPath = "1";
                // ClassTask task1 = realm.copyToRealm(task);
//                AdapterRecycler adapter = new AdapterRecycler(task);
//                adapter.addData(task);
//                adapter.notifyDataSetChanged();
                // new AdapterRecycler().addData(task);
            }
        });


        SharedPreferences.Editor editor = preferences.edit();
        long id1 = id + 1;
        int serial1 = serial+1;
        editor.putInt("serial",serial1);
        editor.putLong("id", id1);
        editor.commit();
        editor.apply();

        Toast.makeText(getActivity(), "Alarm is set", Toast
                .LENGTH_LONG).show();


    }


//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.list:
//                startActivity(new Intent(getActivity(), TaskList.class));
//                break;
//            // action with ID action_settings was selected
//            case R.id.aboutus:
//
//                break;
//            default:
//                break;
//        }
//
//        return true;
//    }


    public FragmentSetTask() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
