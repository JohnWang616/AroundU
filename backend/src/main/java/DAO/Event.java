package DAO;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;

@Data
public class Event {

    public long event_id;
    public String event_code;
    public String event_name;
    public String description;
    public long host_id;
    public boolean isPublic;
    public String location_name;
    public float latitude;
    public float longitude;
    public int max_participants;
    public int curr_num_participants;

    public String photoID;
    public String address;
    public String title;
    //public String type;

    public Timestamp start_time;
    public Timestamp end_time;
    public Timestamp created_at;
    public Timestamp deleted_at;
    public Timestamp updated_at;

    public Event(long id, String event_code, String event_name, String description, long creator_id, boolean isPublic,
                 String location_name, float latitude, float longitude, int max_participants, int curr_participants,
                 String photoID, String address, String title, Timestamp start_time, Timestamp end_time,
                 Timestamp created_at, Timestamp deleted_at, Timestamp updated_at) {

        this.event_id = id;
        this.event_code = event_code;
        this.event_name = event_name;
        this.description = description;
        this.host_id = creator_id;
        this.isPublic = isPublic;
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.max_participants = max_participants;
        this.curr_num_participants = curr_participants;
        this.photoID = photoID;
        this.address = address;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "DAO.Event{" +
                "id=" + event_id +
                ", event_code='" + event_code + '\'' +
                ", event_name='" + event_name + '\'' +
                ", description='" + description + '\'' +
                ", creator_id=" + host_id +
                ", IsPublic=" + isPublic +
                ", location_name='" + location_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", max_participants=" + max_participants +
                ", photoID=" + photoID +
                ", address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", created_at=" + created_at +
                ", deleted_at=" + deleted_at +
                ", updated_at=" + updated_at +
                '}';
    }


}
