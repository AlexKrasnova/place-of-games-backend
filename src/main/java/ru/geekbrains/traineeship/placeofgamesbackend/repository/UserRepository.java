package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.traineeship.placeofgamesbackend.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("Select u from User u join fetch u.roles where u.login = :login")
    User findByLoginWithRoles(String login);
}
