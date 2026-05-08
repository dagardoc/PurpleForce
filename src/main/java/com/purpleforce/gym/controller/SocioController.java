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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/socio")
@RequiredArgsConstructor
public class SocioController {

    private final UsuarioService usuarioService;
    private final ClaseService claseService;
    private final ReservaService reservaService;
    private final InformeService informeService;

    // Obtiene el usuario autenticado actual
    private Usuario getUsuarioActual(UserDetails userDetails) {
        return usuarioService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = getUsuarioActual(userDetails);
        List<Reserva> misReservas = reservaService.findHistorial(usuario);
        List<Clase> clasesDisponibles = claseService.findProximas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("misReservas", misReservas.stream().limit(3).toList());
        model.addAttribute("clasesDisponibles", clasesDisponibles.stream().limit(6).toList());
        model.addAttribute("totalReservas", misReservas.size());
        return "socio/dashboard";
    }

    @GetMapping("/clases")
    public String verClases(@RequestParam(required = false) String nivel,
                            @RequestParam(required = false) String fecha,
                            Model model) {
        LocalDate fechaFiltro = (fecha != null && !fecha.isEmpty()) ? LocalDate.parse(fecha) : null;
        List<Clase> clases = claseService.findByFiltros(nivel, fechaFiltro);

        model.addAttribute("clases", clases);
        model.addAttribute("nivelFiltro", nivel);
        model.addAttribute("fechaFiltro", fecha);
        return "socio/clases";
    }

    @PostMapping("/clases/{id}/reservar")
    public String reservar(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes ra) {
        Usuario usuario = getUsuarioActual(userDetails);
        Clase clase = claseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        try {
            reservaService.reservar(clase, usuario);
            ra.addFlashAttribute("exito", "✅ Reserva realizada correctamente en " + clase.getTipoClase().getNombre());
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", "❌ " + e.getMessage());
        }
        return "redirect:/socio/clases";
    }

    @GetMapping("/mis-reservas")
    public String misReservas(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = getUsuarioActual(userDetails);
        model.addAttribute("reservas", reservaService.findByUsuario(usuario));
        model.addAttribute("usuario", usuario);
        return "socio/mis-reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes ra) {
        Usuario usuario = getUsuarioActual(userDetails);
        try {
            reservaService.cancelar(id, usuario);
            ra.addFlashAttribute("exito", "Reserva cancelada correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/socio/mis-reservas";
    }

    @GetMapping("/mis-reservas/pdf")
    public ResponseEntity<byte[]> descargarHistorialPDF(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        List<Reserva> reservas = reservaService.findByUsuario(usuario);
        try {
            byte[] pdf = informeService.generarHistorialPDF(usuario, reservas);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=historial-" + usuario.getNombre() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("ranking", claseService.getRankingClases());
        return "socio/ranking";
    }
}
