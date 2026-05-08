package com.purpleforce.gym.repository;

import com.purpleforce.gym.model.Rol;
import com.purpleforce.gym.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByActivoTrue();

    boolean existsByEmail(String email);

    // Consulta no trivial: socios con más reservas confirmadas
    @Query("SELECT u, COUNT(r) as totalReservas FROM Usuario u " +
           "JOIN u.reservas r WHERE r.estado = 'CONFIRMADA' " +
           "GROUP BY u ORDER BY totalReservas DESC")
    List<Object[]> findSociosConMasReservas();
}
