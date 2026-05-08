package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    List<Ejercicio> findByGrupoMuscular(String grupoMuscular);
    List<Ejercicio> findByDificultad(String dificultad);
    List<Ejercicio> findByGrupoMuscularAndDificultad(String grupoMuscular, String dificultad);
}
