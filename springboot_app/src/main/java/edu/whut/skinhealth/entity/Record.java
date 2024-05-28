package edu.whut.skinhealth.entity;

import java.sql.Timestamp;
import edu.whut.skinhealth.po.UserInfo;
import jakarta.persistence.*;
@Entity
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = UserInfo.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfo user;
    private String disease;
    private Timestamp time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisease() {
        return disease;
    }

    public Timestamp getTime() {
        return time;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
