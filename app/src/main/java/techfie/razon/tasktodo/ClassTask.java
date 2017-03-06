package techfie.razon.tasktodo;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by razon30 on 28-11-16.
 */
@RealmClass
public class ClassTask extends RealmObject {

    public ClassTask(){};

    String note, time, timeafter, done, audioPath;
    String id;

    public ClassTask(String note, String time, String timeafter, String id, String done, String audioPath) {
        this.note = note;
        this.time = time;
        this.timeafter = timeafter;
        this.id = id;
        this.done = done;
        this.audioPath = audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
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

    public String getTimeafter() {
        return timeafter;
    }

    public void setTimeafter(String timeafter) {
        this.timeafter = timeafter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
