package com.purpleforce.gym.controller;

import com.purpleforce.gym.model.*;
import com.purpleforce.gym.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;
    private final ClaseService claseService;
    private final EntrenadorService entrenadorService;
    private final EjercicioService ejercicioService;
    private final ReservaService reservaService;
    private final InformeService informeService;

    //DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalSocios", usuarioService.findByRol(Rol.SOCIO).size());
        model.addAttribute("totalClases", claseService.findProximas().size());
        model.addAttribute("totalEntrenadores", entrenadorService.findActivos().size());
        model.addAttribute("rankingClases", claseService.getRankingClases());
        model.addAttribute("proximasClases", claseService.findProximas().stream().limit(5).toList());
        return "admin/dashboard";
    }

    //USUARIOS
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/nuevo")
    public String crearUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                               BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Rol.values());
            return "admin/usuario-form";
        }
        try {
            usuarioService.crearUsuario(usuario);
            ra.addFlashAttribute("exito", "Usuario creado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable Long id, RedirectAttributes ra) {
        usuarioService.desactivar(id);
        ra.addFlashAttribute("exito", "Usuario desactivado");
        return "redirect:/admin/usuarios";
    }

    //ENTRENADORES
    @GetMapping("/entrenadores")
    public String entrenadores(Model model) {
        model.addAttribute("entrenadores", entrenadorService.findAll());
        return "admin/entrenadores";
    }

    @GetMapping("/entrenadores/nuevo")
    public String nuevoEntrenador(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "admin/entrenador-form";
    }

    @GetMapping("/entrenadores/{id}/editar")
    public String editarEntrenador(@PathVariable Long id, Model model) {
        entrenadorService.findById(id).ifPresent(e -> model.addAttribute("entrenador", e));
        return "admin/entrenador-form";
    }

    @PostMapping("/entrenadores/guardar")
    public String guardarEntrenador(@Valid @ModelAttribute("entrenador") Entrenador entrenador,
                                    BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "admin/entrenador-form";
        entrenadorService.guardar(entrenador);
        ra.addFlashAttribute("exito", "Entrenador guardado correctamente");
        return "redirect:/admin/entrenadores";
    }

    @PostMapping("/entrenadores/{id}/eliminar")
    public String eliminarEntrenador(@PathVariable Long id, RedirectAttributes ra) {
        entrenadorService.desactivar(id);
        ra.addFlashAttribute("exito", "Entrenador desactivado");
        return "redirect:/admin/entrenadores";
    }

    //TIPOS DE CLASE
    @GetMapping("/tipos-clase")
    public String tiposClase(Model model) {
        model.addAttribute("tipos", claseService.findAllTipos());
        return "admin/tipos-clase";
    }

    @GetMapping("/tipos-clase/nuevo")
    public String nuevoTipo(Model model) {
        model.addAttribute("tipo", new TipoClase());
        return "admin/tipo-clase-form";
    }

    @GetMapping("/tipos-clase/{id}/editar")
    public String editarTipo(@PathVariable Long id, Model model) {
        claseService.findTipoById(id).ifPresent(t -> model.addAttribute("tipo", t));
        return "admin/tipo-clase-form";
    }

    @PostMapping("/tipos-clase/guardar")
    public String guardarTipo(@Valid @ModelAttribute("tipo") TipoClase tipo,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "admin/tipo-clase-form";
        claseService.guardarTipo(tipo);
        ra.addFlashAttribute("exito", "Tipo de clase guardado correctamente");
        return "redirect:/admin/tipos-clase";
    }

    @PostMapping("/tipos-clase/{id}/eliminar")
    public String eliminarTipo(@PathVariable Long id, RedirectAttributes ra) {
        claseService.eliminarTipo(id);
        ra.addFlashAttribute("exito", "Tipo de clase eliminado");
        return "redirect:/admin/tipos-clase";
    }

    //CLASES (SESIONES)
    @GetMapping("/clases")
    public String clases(Model model) {
        model.addAttribute("clases", claseService.findProximas());
        return "admin/clases";
    }

    @GetMapping("/clases/nueva")
    public String nuevaClase(Model model) {
        model.addAttribute("clase", new Clase());
        model.addAttribute("tipos", claseService.findAllTipos());
        model.addAttribute("entrenadores", entrenadorService.findActivos());
        return "admin/clase-form";
    }

    @GetMapping("/clases/{id}/editar")
    public String editarClase(@PathVariable Long id, Model model) {
        claseService.findById(id).ifPresent(c -> model.addAttribute("clase", c));
        model.addAttribute("tipos", claseService.findAllTipos());
        model.addAttribute("entrenadores", entrenadorService.findActivos());
        return "admin/clase-form";
    }

    @PostMapping("/clases/guardar")
    public String guardarClase(@Valid @ModelAttribute("clase") Clase clase,
                               BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tipos", claseService.findAllTipos());
            model.addAttribute("entrenadores", entrenadorService.findActivos());
            return "admin/clase-form";
        }
        claseService.guardar(clase);
        ra.addFlashAttribute("exito", "Clase guardada correctamente");
        return "redirect:/admin/clases";
    }

    @PostMapping("/clases/{id}/cancelar")
    public String cancelarClase(@PathVariable Long id, RedirectAttributes ra) {
        claseService.desactivar(id);
        ra.addFlashAttribute("exito", "Clase cancelada");
        return "redirect:/admin/clases";
    }

    //EJERCICIOS
    @GetMapping("/ejercicios")
    public String ejercicios(@RequestParam(required = false) String grupo,
                             @RequestParam(required = false) String dificultad,
                             Model model) {
        model.addAttribute("ejercicios", ejercicioService.findByFiltros(grupo, dificultad));
        model.addAttribute("grupoFiltro", grupo);
        model.addAttribute("dificultadFiltro", dificultad);
        return "admin/ejercicios";
    }

    @GetMapping("/ejercicios/nuevo")
    public String nuevoEjercicio(Model model) {
        model.addAttribute("ejercicio", new Ejercicio());
        return "admin/ejercicio-form";
    }

    @GetMapping("/ejercicios/{id}/editar")
    public String editarEjercicio(@PathVariable Long id, Model model) {
        ejercicioService.findById(id).ifPresent(e -> model.addAttribute("ejercicio", e));
        return "admin/ejercicio-form";
    }

    @PostMapping("/ejercicios/guardar")
    public String guardarEjercicio(@Valid @ModelAttribute("ejercicio") Ejercicio ejercicio,
                                   BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "admin/ejercicio-form";
        ejercicioService.guardar(ejercicio);
        ra.addFlashAttribute("exito", "Ejercicio guardado correctamente");
        return "redirect:/admin/ejercicios";
    }

    @PostMapping("/ejercicios/{id}/eliminar")
    public String eliminarEjercicio(@PathVariable Long id, RedirectAttributes ra) {
        ejercicioService.eliminar(id);
        ra.addFlashAttribute("exito", "Ejercicio eliminado");
        return "redirect:/admin/ejercicios";
    }

    //INFORMES
    @GetMapping("/informes")
    public String informes(Model model) {
        model.addAttribute("ranking", claseService.getRankingClases());
        model.addAttribute("ocupacion", claseService.getOcupacionMedia());
        model.addAttribute("rankingSocios", usuarioService.getRankingSocios());
        return "admin/informes";
    }

    @GetMapping("/informes/ranking-csv")
    public ResponseEntity<byte[]> descargarRankingCSV() {
        try {
            List<Object[]> ranking = claseService.getRankingClases();
            byte[] csv = informeService.generarRankingCSV(ranking);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ranking-clases.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csv);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
