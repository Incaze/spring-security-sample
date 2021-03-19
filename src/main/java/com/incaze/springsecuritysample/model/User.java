package com.incaze.springsecuritysample.model;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String password;

    @Column(unique=true)
    private String login;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}