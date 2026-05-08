package com.purpleforce.gym.controller;

import com.purpleforce.gym.model.Usuario;
import com.purpleforce.gym.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Email o contraseña incorrectos");
        if (logout != null) model.addAttribute("logout", "Sesión cerrada correctamente");
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult result,
                            RedirectAttributes ra,
                            Model model) {
        if (result.hasErrors()) {
            return "auth/registro";
        }
        try {
            usuarioService.registrarSocio(usuario);
            ra.addFlashAttribute("exito", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/registro";
        }
    }
}
