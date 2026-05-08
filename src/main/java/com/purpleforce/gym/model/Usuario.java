package com.purpleforce.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_alta")
    private LocalDate fechaAlta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}
