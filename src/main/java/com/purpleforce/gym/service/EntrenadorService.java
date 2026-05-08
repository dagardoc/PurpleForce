package com.purpleforce.gym.service;

import com.purpleforce.gym.model.Entrenador;
import com.purpleforce.gym.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    public List<Entrenador> findAll() {
        return entrenadorRepository.findAll();
    }

    public List<Entrenador> findActivos() {
        return entrenadorRepository.findByActivoTrue();
    }

    public Optional<Entrenador> findById(Long id) {
        return entrenadorRepository.findById(id);
    }

    public Optional<Entrenador> findByEmail(String email) {
        return entrenadorRepository.findAll().stream()
                .filter(e -> e.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Transactional
    public Entrenador guardar(Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    @Transactional
    public void desactivar(Long id) {
        entrenadorRepository.findById(id).ifPresent(e -> {
            e.setActivo(false);
            entrenadorRepository.save(e);
        });
    }

    @Transactional
    public void eliminar(Long id) {
        entrenadorRepository.deleteById(id);
    }
}
