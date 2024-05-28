package edu.whut.skinhealth.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class RecordRequest {
    private String username;

    private String disease;
    private Timestamp time;


    public String getDisease() {
        return disease;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
