package com.incaze.springsecuritysample.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    private String password;

    @Column(unique=true)
    private String login;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}