package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("Select u from User u join fetch u.roles where u.login = :login")
    User findByLoginWithRoles(String login);

    Optional<User> findByLogin(String login);

}
