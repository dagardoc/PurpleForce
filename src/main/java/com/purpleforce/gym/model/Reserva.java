package com.purpleforce.gym.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    private String estado; // CONFIRMADA, CANCELADA

    @PrePersist
    public void prePersist() {
        if (fechaReserva == null) {
            fechaReserva = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "CONFIRMADA";
        }
    }
}
