package ru.geekbrains.traineeship.placeofgamesbackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkingHours> workingHoursList;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<Event> events;

    @Version
    private Long version;

}
