package techfie.razon.tasktodo;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by razon30 on 30-11-16.
 */

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler
        .ViewHolderAdapterRecycler>  {

    ArrayList<ClassTask> activityList;
    Context context;
    private LayoutInflater layoutInflater;
    String classType = "1";

    Realm realm;

    public AdapterRecycler(ArrayList<ClassTask> activityList, Context context, String classType) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        this.activityList = activityList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.classType = classType;
    }


    @Override
    public ViewHolderAdapterRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_recycler, parent, false);
        ViewHolderAdapterRecycler viewHolder = new ViewHolderAdapterRecycler(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapterRecycler holder, int position) {

        ClassTask currentTask = activityList.get(position);

        String timefor = currentTask.getTimeafter();
        String month,date,time,tempdate,temptime;

        holder.note.setText(currentTask.getNote());
     //  holder.time.setText(currentTask.getTime());
        if (timefor.length()>15){

            temptime = timefor.substring(0,14);
            tempdate = timefor.substring(18,timefor.length());

            month = tempdate.substring(5,8);
            date = tempdate.substring(1,4);

            holder.date.setText(date);
            holder.time.setText(temptime);
            holder.month.setText(month);

        }else {
            month = "Aftr";
            String[] times = timefor.split(" ");
            date = times[0];
            holder.time.setText("After "+currentTask.getTimeafter());
            holder.date.setText(date);
            holder.month.setText(month);
        }

        final String path = currentTask.getAudioPath();
        holder.recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path.equals("1")){
                    Toast.makeText(context, "No Audio Available", Toast.LENGTH_SHORT).show();
                }else {

                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(path);
                    m.prepare();
                    m.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                }
            }
        });


        if (position%6==0){
           setTheme(holder.month,holder.date,holder.note,holder.view,"#ff4444", R.drawable
                   .circula_one,holder.recordView,R.drawable.record_one);
        }else if (position%6==1){
            setTheme(holder.month,holder.date,holder.note,holder.view, "#00C851", R.drawable
                    .circula_two,holder.recordView,R.drawable.record_two);
        }else if (position%6==2){
            setTheme(holder.month,holder.date,holder.note,holder.view,"#33b5e5", R.drawable
                    .circula_three,holder.recordView,R.drawable.record_three);
        }else if (position%6==3){
            setTheme(holder.month,holder.date,holder.note,holder.view,"#f472d0", R.drawable
                    .circula_four,holder.recordView,R.drawable.record_four);
        }else if (position%6==4){
            setTheme(holder.month,holder.date,holder.note,holder.view,"#3D5AFE", R.drawable
                    .circula_five,holder.recordView,R.drawable.record_five);
        }else if (position%6==5){
            setTheme(holder.month,holder.date,holder.note,holder.view,"#FF8800", R.drawable
                    .circula_six,holder.recordView,R.drawable.record_six);
        }else {
            setTheme(holder.month,holder.date,holder.note,holder.view,"#00695c", R.drawable
                    .circula_four,holder.recordView,R.drawable.record_four);
        }

    }

    private void setTheme(TextView month, TextView date, TextView note, View view, String color, int circula_one, ImageView recordView, int record_four) {

        month.setTextColor(Color.parseColor(color));
        date.setBackgroundResource(circula_one);
        note.setTextColor(Color.parseColor(color));
        view.setBackgroundColor(Color.parseColor(color));
        recordView.setImageResource(record_four);

    }

    public void addItem(int position, final ClassTask classTask){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClassTask classTask1 = realm.copyToRealm(classTask);
            }
        });
        activityList.add(position,classTask);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, activityList.size()+1);
    }


    public void removeItem(int position) {

       final String id = activityList.get(position).getId();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm.where(ClassTask.class).equalTo("id",id).findFirst()!=null) {
                    realm.where(ClassTask.class).equalTo("id", id).findFirst().deleteFromRealm();
                }
            }
        });
        activityList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, activityList.size());
    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ViewHolderAdapterRecycler extends RecyclerView.ViewHolder {

        TextView note,time, month, date;
        View view;
        ImageView recordView;

        public ViewHolderAdapterRecycler(View itemView) {
            super(itemView);

            note = (TextView) itemView.findViewById(R.id.note);
            time = (TextView) itemView.findViewById(R.id.time);
            view = itemView.findViewById(R.id.view);
            month = (TextView) itemView.findViewById(R.id.month);
            date = (TextView) itemView.findViewById(R.id.date);
            recordView = (ImageView) itemView.findViewById(R.id.record_task);

        }
    }
}