package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
