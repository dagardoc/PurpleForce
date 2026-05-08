package com.purpleforce.gym.service;

import com.purpleforce.gym.model.*;
import com.purpleforce.gym.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final TipoClaseRepository tipoClaseRepository;
    private final EntrenadorRepository entrenadorRepository;

    public List<Clase> findAll() {
        return claseRepository.findAll();
    }

    public List<Clase> findProximas() {
        return claseRepository.findClasesDisponibles(LocalDate.now());
    }

    public List<Clase> findByFiltros(String nivel, LocalDate fecha) {
        return claseRepository.findByFiltros(nivel, fecha);
    }

    public Optional<Clase> findById(Long id) {
        return claseRepository.findById(id);
    }

    public List<Clase> findByEntrenador(Entrenador entrenador) {
        return claseRepository.findByEntrenadorAndActivaTrue(entrenador);
    }

    @Transactional
    public Clase guardar(Clase clase) {
        return claseRepository.save(clase);
    }

    @Transactional
    public void desactivar(Long id) {
        claseRepository.findById(id).ifPresent(c -> {
            c.setActiva(false);
            claseRepository.save(c);
        });
    }

    public List<TipoClase> findAllTipos() {
        return tipoClaseRepository.findAll();
    }

    public Optional<TipoClase> findTipoById(Long id) {
        return tipoClaseRepository.findById(id);
    }

    @Transactional
    public TipoClase guardarTipo(TipoClase tipo) {
        return tipoClaseRepository.save(tipo);
    }

    @Transactional
    public void eliminarTipo(Long id) {
        tipoClaseRepository.deleteById(id);
    }

    // Ranking de clases más populares por reservas
    public List<Object[]> getRankingClases() {
        return tipoClaseRepository.findRankingPorReservas();
    }

    // Ocupación media por tipo de clase
    public List<Object[]> getOcupacionMedia() {
        return tipoClaseRepository.findOcupacionMediaPorTipo();
    }

    public List<Entrenador> findAllEntrenadores() {
        return entrenadorRepository.findByActivoTrue();
    }
}
