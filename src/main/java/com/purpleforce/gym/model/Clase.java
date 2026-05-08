package com.purpleforce.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_clase_id", nullable = false)
    @NotNull(message = "El tipo de clase es obligatorio")
    private TipoClase tipoClase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrenador_id", nullable = false)
    @NotNull(message = "El entrenador es obligatorio")
    private Entrenador entrenador;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Min(value = 1, message = "El aforo mínimo es 1")
    @Column(name = "aforo_maximo", nullable = false)
    private int aforoMaximo;

    @Size(max = 50)
    private String sala;

    @Column(nullable = false)
    private boolean activa = true;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    // Número de plazas reservadas
    public int getPlazasOcupadas() {
        if (reservas == null) return 0;
        return (int) reservas.stream()
                .filter(r -> r.getEstado().equals("CONFIRMADA"))
                .count();
    }

    // Plazas libres disponibles
    public int getPlazasLibres() {
        return aforoMaximo - getPlazasOcupadas();
    }

    // ¿Hay plazas disponibles?
    public boolean isDisponible() {
        return getPlazasLibres() > 0 && activa;
    }
}
