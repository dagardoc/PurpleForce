package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    List<Entrenador> findByActivoTrue();
    boolean existsByEmail(String email);
}
