package techfie.razon.tasktodo;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import io.realm.Realm;

public class AlarmDialouge extends Activity {

    TextView tvNote, tvDate;
    Button btnStop;
    private NotificationManager mManager;
    Uri notification;
    Ringtone r;
    MediaPlayer m;
    String classDefine;
    String id;
    Realm realm;

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view = inflater.inflate(R.layout.activity_alarm_dialouge,null);
//
//      //  setContentView(R.layout.activity_alarm_dialouge);
//        tvNote = (TextView)view. findViewById(R.id.tvNote);
//        tvDate = (TextView)view. findViewById(R.id.tvDate);
//        btnStop = (Button)view. findViewById(R.id.ok);
//        Intent intent = getActivity().getIntent();
//        tvNote.setText(intent.getStringExtra("note"));
//        tvDate.setText(intent.getStringExtra("time"));
//
//        try {
//            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//            r = RingtoneManager.getRingtone(getActivity(), notification);
//            r.play();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                r.stop();
//                getActivity().finish();
//            }
//        });
//
//        return super.onCreateDialog(savedInstanceState);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm_dialouge);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnStop = (Button) findViewById(R.id.ok);
        Intent intent = getIntent();
        classDefine = intent.getStringExtra("class");
        id = intent.getStringExtra("id");
        tvNote.setText(intent.getStringExtra("note"));
        tvDate.setText(intent.getStringExtra("time"));

        if (classDefine.equals("0")){
            ClassTask task = realm.where(ClassTask.class).equalTo("id",id).findFirst();
            String path = task.getAudioPath();
            if (path.equals("1")){
                try {
                    notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    r = RingtoneManager.getRingtone(this, notification);
                    r.play();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                m = new MediaPlayer();

                try {
                    m.setDataSource(path);
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
            }
        }else {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ClassToDo classToDo = realm.where(ClassToDo.class).equalTo("id",id).findFirst();
                    classToDo.setDone(true);
                }
            });

            try {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                r = RingtoneManager.getRingtone(this, notification);
                r.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (r!=null) {
                    r.stop();
                }
                if (m!=null){
                    m.stop();
                }
                finish();
            }
        });

    }
}
