package com.purpleforce.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ejercicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotBlank(message = "El grupo muscular es obligatorio")
    @Column(name = "grupo_muscular", nullable = false)
    private String grupoMuscular; // PIERNAS, PECHO, ESPALDA, BRAZOS, CORE, CUERPO_COMPLETO

    @NotBlank(message = "La dificultad es obligatoria")
    @Column(nullable = false)
    private String dificultad; // BASICO, INTERMEDIO, AVANZADO

    @Size(max = 200)
    @Column(name = "material_necesario")
    private String materialNecesario;
}
