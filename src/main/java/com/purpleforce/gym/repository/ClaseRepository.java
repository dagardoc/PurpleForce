package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.Clase;
import com.purpleforce.gym.model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    List<Clase> findByFechaGreaterThanEqualAndActivaTrueOrderByFechaAscHoraInicioAsc(LocalDate fecha);

    List<Clase> findByEntrenadorAndActivaTrue(Entrenador entrenador);

    List<Clase> findByFechaAndActivaTrue(LocalDate fecha);

    // Filtro combinado por nivel y fecha (consulta no trivial)
    @Query("SELECT c FROM Clase c JOIN c.tipoClase tc " +
           "WHERE (:nivel IS NULL OR tc.nivel = :nivel) " +
           "AND (:fecha IS NULL OR c.fecha = :fecha) " +
           "AND c.activa = true " +
           "ORDER BY c.fecha ASC, c.horaInicio ASC")
    List<Clase> findByFiltros(@Param("nivel") String nivel,
                               @Param("fecha") LocalDate fecha);

    // Clases próximas (hoy en adelante) con plazas libres
    @Query("SELECT c FROM Clase c WHERE c.fecha >= :hoy AND c.activa = true " +
           "ORDER BY c.fecha ASC, c.horaInicio ASC")
    List<Clase> findClasesDisponibles(@Param("hoy") LocalDate hoy);
}
