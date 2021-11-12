package ru.geekbrains.traineeship.placeofgamesbackend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role_table")
public class Role {

    @Id
    @Column(name = "name")
    private String name;

}
