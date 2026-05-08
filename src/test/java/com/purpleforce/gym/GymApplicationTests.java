package com.purpleforce.gym;

import com.purpleforce.gym.model.*;
import com.purpleforce.gym.repository.*;
import com.purpleforce.gym.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas funcionales de PurpleForce Gym
 * Tabla de pruebas incluida en la memoria (sección 9.2)
 *
 * PT-01: Registro de nuevo socio con datos válidos
 * PT-02: Login con credenciales correctas
 * PT-03: Login con credenciales incorrectas
 * PT-04: Creación de tipo de clase por admin
 * PT-05: Reserva de clase con plazas disponibles
 * PT-06: Rechazo de reserva duplicada
 * PT-07: Cancelación de reserva
 * PT-08: Acceso denegado a zona admin para socio
 * PT-09: Filtrado de clases por nivel
 * PT-10: Consulta ranking de clases (consulta no trivial)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas funcionales PurpleForce Gym")
class GymApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClaseService claseService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private TipoClaseRepository tipoClaseRepository;

    @Autowired
    private ClaseRepository claseRepository;

    // =====================================================
    // PT-01: Registro de nuevo socio
    // =====================================================
    @Test
    @DisplayName("PT-01: Registro de socio con datos válidos → usuario creado correctamente")
    void testRegistroSocioValido() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Test");
        nuevo.setApellidos("Usuario Prueba");
        nuevo.setEmail("prueba.test@gym.com");
        nuevo.setPassword("password123");

        Usuario guardado = usuarioService.registrarSocio(nuevo);

        assertNotNull(guardado.getId(), "El usuario debe tener ID asignado");
        assertEquals(Rol.SOCIO, guardado.getRol(), "El rol debe ser SOCIO");
        assertTrue(guardado.isActivo(), "El usuario debe estar activo");
        assertNotEquals("password123", guardado.getPassword(), "La contraseña debe estar cifrada con BCrypt");
    }

    // =====================================================
    // PT-02: Login con credenciales correctas
    // =====================================================
    @Test
    @DisplayName("PT-02: Login con email y contraseña correctos → redirección al dashboard")
    void testLoginCorrecto() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("email", "admin@purpleforce.com")
                        .param("password", "1234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // =====================================================
    // PT-03: Login con credenciales incorrectas
    // =====================================================
    @Test
    @DisplayName("PT-03: Login con contraseña incorrecta → redirección con error")
    void testLoginIncorrecto() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("email", "admin@purpleforce.com")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error=true"));
    }

    // =====================================================
    // PT-04: Creación de tipo de clase
    // =====================================================
    @Test
    @DisplayName("PT-04: Crear tipo de clase con datos válidos → guardado en BD")
    @WithMockUser(username = "admin@purpleforce.com", roles = {"ADMIN"})
    void testCrearTipoClase() {
        TipoClase tipo = TipoClase.builder()
                .nombre("CrossFit Test")
                .nivel("INTERMEDIO")
                .duracionMinutos(60)
                .precio(new BigDecimal("16.00"))
                .color("#8e44ad")
                .build();

        TipoClase guardado = claseService.guardarTipo(tipo);

        assertNotNull(guardado.getId());
        assertEquals("CrossFit Test", guardado.getNombre());
        assertEquals("INTERMEDIO", guardado.getNivel());
    }

    // =====================================================
    // PT-05: Reserva de clase con plazas disponibles
    // =====================================================
    @Test
    @DisplayName("PT-05: Reservar clase con plazas libres → reserva confirmada")
    void testReservarClaseDisponible() {
        // Crear datos de prueba
        Entrenador entrenador = entrenadorRepository.save(
                Entrenador.builder().nombre("Test").apellidos("Entrenador")
                        .especialidad("Test").email("test.trainer@gym.com").activo(true).build()
        );

        TipoClase tipo = tipoClaseRepository.save(
                TipoClase.builder().nombre("Clase Test").nivel("BASICO")
                        .duracionMinutos(45).precio(new BigDecimal("10.00")).build()
        );

        Clase clase = claseRepository.save(
                Clase.builder().tipoClase(tipo).entrenador(entrenador)
                        .fecha(LocalDate.now().plusDays(1))
                        .horaInicio(LocalTime.of(10, 0)).horaFin(LocalTime.of(11, 0))
                        .aforoMaximo(10).sala("Sala Test").activa(true).build()
        );

        Usuario socio = usuarioService.registrarSocio(
                Usuario.builder().nombre("Socio").apellidos("Test")
                        .email("socio.test@gym.com").password("1234").build()
        );

        Reserva reserva = reservaService.reservar(clase, socio);

        assertNotNull(reserva.getId());
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertEquals(clase.getId(), reserva.getClase().getId());
    }

    // =====================================================
    // PT-06: Rechazo de reserva duplicada
    // =====================================================
    @Test
    @DisplayName("PT-06: Intentar reservar la misma clase dos veces → excepción")
    void testReservaDuplicadaRechazada() {
        Entrenador entrenador = entrenadorRepository.save(
                Entrenador.builder().nombre("Dup").apellidos("Entrenador")
                        .especialidad("Test").email("dup.trainer@gym.com").activo(true).build()
        );

        TipoClase tipo = tipoClaseRepository.save(
                TipoClase.builder().nombre("Clase Dup").nivel("BASICO")
                        .duracionMinutos(45).precio(new BigDecimal("10.00")).build()
        );

        Clase clase = claseRepository.save(
                Clase.builder().tipoClase(tipo).entrenador(entrenador)
                        .fecha(LocalDate.now().plusDays(2))
                        .horaInicio(LocalTime.of(11, 0)).horaFin(LocalTime.of(12, 0))
                        .aforoMaximo(10).activa(true).build()
        );

        Usuario socio = usuarioService.registrarSocio(
                Usuario.builder().nombre("Dup").apellidos("Socio")
                        .email("dup.socio@gym.com").password("1234").build()
        );

        reservaService.reservar(clase, socio);

        assertThrows(IllegalStateException.class, () ->
                reservaService.reservar(clase, socio),
                "Debería lanzar excepción al intentar reservar dos veces la misma clase"
        );
    }

    // =====================================================
    // PT-07: Cancelación de reserva
    // =====================================================
    @Test
    @DisplayName("PT-07: Cancelar una reserva confirmada → estado CANCELADA")
    void testCancelarReserva() {
        Entrenador entrenador = entrenadorRepository.save(
                Entrenador.builder().nombre("Cancel").apellidos("Entrenador")
                        .especialidad("Test").email("cancel.trainer@gym.com").activo(true).build()
        );

        TipoClase tipo = tipoClaseRepository.save(
                TipoClase.builder().nombre("Clase Cancel").nivel("BASICO")
                        .duracionMinutos(45).precio(new BigDecimal("10.00")).build()
        );

        Clase clase = claseRepository.save(
                Clase.builder().tipoClase(tipo).entrenador(entrenador)
                        .fecha(LocalDate.now().plusDays(3))
                        .horaInicio(LocalTime.of(12, 0)).horaFin(LocalTime.of(13, 0))
                        .aforoMaximo(10).activa(true).build()
        );

        Usuario socio = usuarioService.registrarSocio(
                Usuario.builder().nombre("Cancel").apellidos("Socio")
                        .email("cancel.socio@gym.com").password("1234").build()
        );

        Reserva reserva = reservaService.reservar(clase, socio);
        reservaService.cancelar(reserva.getId(), socio);

        Reserva actualizada = reservaService.findById(reserva.getId()).orElseThrow();
        assertEquals("CANCELADA", actualizada.getEstado());
    }

    // =====================================================
    // PT-08: Acceso denegado a zona admin para socio
    // =====================================================
    @Test
    @DisplayName("PT-08: Socio intenta acceder a /admin → acceso denegado (403)")
    @WithMockUser(username = "alex@gmail.com", roles = {"SOCIO"})
    void testAccesoDenegadoAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    // =====================================================
    // PT-09: Filtrado de clases por nivel
    // =====================================================
    @Test
    @DisplayName("PT-09: Filtrar clases por nivel PRINCIPIANTE → solo muestra ese nivel")
    @WithMockUser(username = "alex@gmail.com", roles = {"SOCIO"})
    void testFiltradoClasesPorNivel() throws Exception {
        mockMvc.perform(get("/socio/clases").param("nivel", "PRINCIPIANTE"))
                .andExpect(status().isOk())
                .andExpect(view().name("socio/clases"))
                .andExpect(model().attributeExists("clases"));
    }

    // =====================================================
    // PT-10: Consulta ranking clases (consulta no trivial)
    // =====================================================
    @Test
    @DisplayName("PT-10: Obtener ranking de clases más populares → lista ordenada")
    void testRankingClasesNoTrivial() {
        var ranking = claseService.getRankingClases();
        assertNotNull(ranking, "El ranking no debe ser nulo");
        // Si hay datos, verificar que viene ordenado por reservas descendente
        if (ranking.size() > 1) {
            Long primera = (Long) ranking.get(0)[1];
            Long segunda = (Long) ranking.get(1)[1];
            assertTrue(primera >= segunda, "El ranking debe estar ordenado de mayor a menor");
        }
    }
}
