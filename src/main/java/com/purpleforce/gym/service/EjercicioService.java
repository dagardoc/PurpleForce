package com.purpleforce.gym.service;

import com.purpleforce.gym.model.Ejercicio;
import com.purpleforce.gym.repository.EjercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public List<Ejercicio> findAll() {
        return ejercicioRepository.findAll();
    }

    public List<Ejercicio> findByFiltros(String grupoMuscular, String dificultad) {
        if (grupoMuscular != null && !grupoMuscular.isEmpty()
                && dificultad != null && !dificultad.isEmpty()) {
            return ejercicioRepository.findByGrupoMuscularAndDificultad(grupoMuscular, dificultad);
        } else if (grupoMuscular != null && !grupoMuscular.isEmpty()) {
            return ejercicioRepository.findByGrupoMuscular(grupoMuscular);
        } else if (dificultad != null && !dificultad.isEmpty()) {
            return ejercicioRepository.findByDificultad(dificultad);
        }
        return ejercicioRepository.findAll();
    }

    public Optional<Ejercicio> findById(Long id) {
        return ejercicioRepository.findById(id);
    }

    @Transactional
    public Ejercicio guardar(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Transactional
    public void eliminar(Long id) {
        ejercicioRepository.deleteById(id);
    }
}
