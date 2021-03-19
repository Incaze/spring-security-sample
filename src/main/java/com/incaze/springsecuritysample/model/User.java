package com.incaze.springsecuritysample.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    private String password;

    @Column(unique=true)
    private String login;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ResponseBody
    public User getUser(){
        return this;
    }
}