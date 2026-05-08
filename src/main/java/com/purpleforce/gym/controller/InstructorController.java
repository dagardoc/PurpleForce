package com.purpleforce.gym.controller;

import com.purpleforce.gym.model.*;
import com.purpleforce.gym.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final UsuarioService usuarioService;
    private final EntrenadorService entrenadorService;
    private final ClaseService claseService;
    private final ReservaService reservaService;
    private final InformeService informeService;

    private Entrenador getEntrenadorActual(UserDetails userDetails) {
        return entrenadorService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Entrenador entrenador = getEntrenadorActual(userDetails);
        List<Clase> misClases = claseService.findByEntrenador(entrenador);

        model.addAttribute("entrenador", entrenador);
        model.addAttribute("misClases", misClases);
        model.addAttribute("totalClases", misClases.size());
        return "instructor/dashboard";
    }

    @GetMapping("/mis-clases")
    public String misClases(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Entrenador entrenador = getEntrenadorActual(userDetails);
        model.addAttribute("clases", claseService.findByEntrenador(entrenador));
        model.addAttribute("entrenador", entrenador);
        return "instructor/mis-clases";
    }

    @GetMapping("/mis-clases/{id}/alumnos")
    public String verAlumnos(@PathVariable Long id, Model model) {
        Clase clase = claseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        List<Reserva> reservas = reservaService.findByClase(clase);
        model.addAttribute("clase", clase);
        model.addAttribute("reservas", reservas);
        return "instructor/alumnos-clase";
    }

    @GetMapping("/horario/pdf")
    public ResponseEntity<byte[]> descargarHorarioPDF(@AuthenticationPrincipal UserDetails userDetails) {
        Entrenador entrenador = getEntrenadorActual(userDetails);
        List<Clase> clases = claseService.findByEntrenador(entrenador);
        try {
            byte[] pdf = informeService.generarHorarioEntrenadorPDF(entrenador, clases);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=horario-" + entrenador.getNombre() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
