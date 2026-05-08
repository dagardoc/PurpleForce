package com.purpleforce.gym.service;

import com.purpleforce.gym.model.Rol;
import com.purpleforce.gym.model.Usuario;
import com.purpleforce.gym.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring Security: carga el usuario por email para autenticación
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findByRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario registrarSocio(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(Rol.SOCIO);
        usuario.setActivo(true);
        usuario.setFechaAlta(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        usuario.setFechaAlta(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void desactivar(Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setActivo(false);
            usuarioRepository.save(u);
        });
    }

    public List<Object[]> getRankingSocios() {
        return usuarioRepository.findSociosConMasReservas();
    }
}
