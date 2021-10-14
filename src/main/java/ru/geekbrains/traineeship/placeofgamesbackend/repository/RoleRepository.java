package ru.geekbrains.traineeship.placeofgamesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.traineeship.placeofgamesbackend.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}