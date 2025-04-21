package ru.minusd.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.minusd.security.domain.model.History;
import ru.minusd.security.domain.model.User;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    public List<History> findByUser(User user);
}
