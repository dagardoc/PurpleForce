package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoClaseRepository extends JpaRepository<TipoClase, Long> {

    List<TipoClase> findByNivel(String nivel);

    // Consulta no trivial 1: Ranking de tipos de clase por número de reservas confirmadas
    @Query("SELECT tc, COUNT(r) as totalReservas FROM TipoClase tc " +
           "JOIN tc.clases c JOIN c.reservas r " +
           "WHERE r.estado = 'CONFIRMADA' " +
           "GROUP BY tc ORDER BY totalReservas DESC")
    List<Object[]> findRankingPorReservas();

    // Consulta no trivial 2: Tipos de clase con ocupación media
    @Query("SELECT tc.nombre, AVG(SIZE(c.reservas)) as ocupacionMedia " +
           "FROM TipoClase tc JOIN tc.clases c " +
           "GROUP BY tc.nombre ORDER BY ocupacionMedia DESC")
    List<Object[]> findOcupacionMediaPorTipo();
}
