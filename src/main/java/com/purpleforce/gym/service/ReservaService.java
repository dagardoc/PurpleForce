package com.purpleforce.gym.service;

import com.purpleforce.gym.model.*;
import com.purpleforce.gym.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClaseRepository claseRepository;

    public List<Reserva> findByUsuario(Usuario usuario) {
        return reservaRepository.findByUsuarioOrderByFechaReservaDesc(usuario);
    }

    public List<Reserva> findHistorial(Usuario usuario) {
        return reservaRepository.findHistorialUsuario(usuario);
    }

    public List<Reserva> findByClase(Clase clase) {
        return reservaRepository.findByClase(clase);
    }

    /**
     * Crea una reserva con control de concurrencia mediante @Transactional.
     * Si dos usuarios intentan reservar la última plaza a la vez,
     * la transacción garantiza que solo una tenga éxito.
     */
    @Transactional
    public Reserva reservar(Clase clase, Usuario usuario) {
        // Verificar que el usuario no tiene ya una reserva confirmada en esta clase
        if (reservaRepository.existsByClaseAndUsuarioAndEstado(clase, usuario, "CONFIRMADA")) {
            throw new IllegalStateException("Ya tienes una reserva confirmada en esta clase");
        }

        // Re-cargar la clase con bloqueo para evitar condición de carrera
        Clase claseActual = claseRepository.findById(clase.getId())
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));

        // Comprobar aforo en tiempo real
        long plazasOcupadas = reservaRepository.countConfirmadasByClase(claseActual);
        if (plazasOcupadas >= claseActual.getAforoMaximo()) {
            throw new IllegalStateException("No quedan plazas disponibles en esta clase");
        }

        if (!claseActual.isActiva()) {
            throw new IllegalStateException("Esta clase ya no está activa");
        }

        Reserva reserva = Reserva.builder()
                .clase(claseActual)
                .usuario(usuario)
                .estado("CONFIRMADA")
                .build();

        return reservaRepository.save(reserva);
    }

    /**
     * Cancela una reserva del usuario.
     */
    @Transactional
    public void cancelar(Long reservaId, Usuario usuario) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Solo el propio usuario o admin puede cancelar
        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("No tienes permiso para cancelar esta reserva");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @Transactional
    public void cancelarAdmin(Long reservaId) {
        reservaRepository.findById(reservaId).ifPresent(r -> {
            r.setEstado("CANCELADA");
            reservaRepository.save(r);
        });
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public long countConfirmadas(Clase clase) {
        return reservaRepository.countConfirmadasByClase(clase);
    }
}
