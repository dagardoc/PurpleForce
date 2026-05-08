package com.purpleforce.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "entrenador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100)
    private String especialidad;

    @Size(max = 15)
    private String telefono;

    @Email(message = "El email no es válido")
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "entrenador")
    private List<Clase> clases;

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}
