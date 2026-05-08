package com.purpleforce.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tipo_clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotBlank(message = "El nivel es obligatorio")
    @Column(nullable = false)
    private String nivel; // PRINCIPIANTE, INTERMEDIO, AVANZADO

    @Min(value = 15, message = "La duración mínima es 15 minutos")
    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal precio;

    @Size(max = 7)
    private String color = "#8e44ad"; // Color morado por defecto

    @OneToMany(mappedBy = "tipoClase")
    private List<Clase> clases;
}
