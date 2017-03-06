package techfie.razon.tasktodo;

import io.realm.RealmObject;

/**
 * Created by razon30 on 15-12-16.
 */

public class ClassToDo extends RealmObject{

    public ClassToDo(){};

    String note, time;
    String id;
    boolean isDone, isAlarmSet;

    public ClassToDo(String note, String time, String id, boolean isDone, boolean isAlarmSet) {
        this.note = note;
        this.time = time;
        this.id = id;
        this.isDone = isDone;
        this.isAlarmSet = isAlarmSet;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        isAlarmSet = alarmSet;
    }
}
