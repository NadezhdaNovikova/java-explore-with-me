package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_server.entity.Type;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {

    @Query("select t from Type t where t.name = :name")
    Optional<Type> findTypeByName(String name);
}
