package com.example.smartmediaschedular;

public class messageData {
    String message_id, receiver_contact, status;
    String sender_name, message, schedule_date, schedule_time;


    public String getSender_name() {
        return sender_name;
    }

    public String getMessage() {
        return message;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public String getSchedule_time() {
        return schedule_time;
    }


    public String getMessage_id() {
        return message_id;
    }

    public String getReceiver_contact() {
        return receiver_contact;
    }

    public String getStatus() {
        return status;
    }
}
