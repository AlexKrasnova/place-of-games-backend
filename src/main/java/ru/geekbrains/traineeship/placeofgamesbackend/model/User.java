package ru.geekbrains.traineeship.placeofgamesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "user_role_link",
            joinColumns = @JoinColumn(name = "user_login"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Collection<Role> roles;
}
