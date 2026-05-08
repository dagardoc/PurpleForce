package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.Clase;
import com.purpleforce.gym.model.Reserva;
import com.purpleforce.gym.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioOrderByFechaReservaDesc(Usuario usuario);

    List<Reserva> findByClase(Clase clase);

    Optional<Reserva> findByClaseAndUsuario(Clase clase, Usuario usuario);

    // Historial del usuario (solo confirmadas, ordenadas por fecha de clase)
    @Query("SELECT r FROM Reserva r WHERE r.usuario = :usuario " +
           "AND r.estado = 'CONFIRMADA' " +
           "ORDER BY r.clase.fecha DESC, r.clase.horaInicio DESC")
    List<Reserva> findHistorialUsuario(@Param("usuario") Usuario usuario);

    // Contar reservas confirmadas por clase
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.clase = :clase AND r.estado = 'CONFIRMADA'")
    long countConfirmadasByClase(@Param("clase") Clase clase);

    boolean existsByClaseAndUsuarioAndEstado(Clase clase, Usuario usuario, String estado);
}
