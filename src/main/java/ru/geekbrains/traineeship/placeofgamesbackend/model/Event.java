package ru.geekbrains.traineeship.placeofgamesbackend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "place_id")
    private Long placeId;

    @ManyToOne
    @JoinColumn(name = "place_id", insertable = false, updatable = false)
    private Place place;

    @Column(name = "max_number_of_participants")
    private Integer maxNumberOfParticipants;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "event_participant",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;

    @Version
    private Long version;
}
