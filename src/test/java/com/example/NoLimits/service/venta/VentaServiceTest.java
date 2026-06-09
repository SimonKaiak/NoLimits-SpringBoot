package com.example.NoLimits.service.venta;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.service.venta.VentaService;
import com.example.NoLimits.config.AbstractContainerBaseTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * VentaServiceTest — Pruebas unitarias del servicio de ventas.
 *
 * Cubre: findAll, findById, findByMetodoPago, findByFechaCompra,
 * findByHoraCompra, findByUsuarioYMetodoPago, save, update, patch,
 * deleteById, crearVentaDesdeRequest.
 */
@SpringBootTest
@ActiveProfiles("test")
public class VentaServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private VentaService ventaService;

    @MockBean private VentaRepository ventaRepository;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private MetodoPagoRepository metodoPagoRepository;
    @MockBean private MetodoEnvioRepository metodoEnvioRepository;
    @MockBean private EstadoRepository estadoRepository;
    @MockBean private ProductoRepository productoRepository;

    // ===================== HELPERS =====================

    private UsuarioModel usuarioEntity() {
        UsuarioModel u = new UsuarioModel();
        u.setId(1L);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        u.setCorreo("juan@test.com");
        u.setTelefono(123456789L);
        u.setPassword("$2a$10$hash");
        return u;
    }

    private MetodoPagoModel metodoPagoEntity() {
        MetodoPagoModel m = new MetodoPagoModel();
        m.setId(1L);
        m.setNombre("Tarjeta de Crédito");
        m.setActivo(true);
        return m;
    }

    private MetodoEnvioModel metodoEnvioEntity() {
        MetodoEnvioModel m = new MetodoEnvioModel();
        m.setId(1L);
        m.setNombre("Despacho a domicilio");
        m.setDescripcion("Entrega en domicilio");
        m.setActivo(true);
        return m;
    }

    private EstadoModel estadoEntity() {
        EstadoModel e = new EstadoModel();
        e.setId(1L);
        e.setNombre("Pagada");
        e.setActivo(true);
        return e;
    }

    private VentaModel ventaEntity() {
        VentaModel v = new VentaModel();
        v.setId(1L);
        v.setFechaCompra(LocalDate.of(2025, 7, 6));
        v.setHoraCompra(LocalTime.of(14, 30));
        v.setUsuarioModel(usuarioEntity());
        v.setMetodoPagoModel(metodoPagoEntity());
        v.setMetodoEnvioModel(metodoEnvioEntity());
        v.setEstado(estadoEntity());
        v.setDetalles(new ArrayList<>());
        return v;
    }

    private VentaModel ventaConCamposRequeridos() {
        VentaModel v = new VentaModel();
        UsuarioModel u = new UsuarioModel(); u.setId(1L);
        MetodoPagoModel mp = new MetodoPagoModel(); mp.setId(1L);
        MetodoEnvioModel me = new MetodoEnvioModel(); me.setId(1L);
        EstadoModel es = new EstadoModel(); es.setId(1L);
        v.setUsuarioModel(u);
        v.setMetodoPagoModel(mp);
        v.setMetodoEnvioModel(me);
        v.setEstado(es);
        return v;
    }


    @Test
    public void testFindAll_DevuelveLista() {
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEntity()));

        List<VentaResponseDTO> result = ventaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_ListaVacia_DevuelveVacio() {
        when(ventaRepository.findAll()).thenReturn(List.of());

        List<VentaResponseDTO> result = ventaService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ===================== FIND BY ID =====================
   
    @Nested 
    @DisplayName("Find Tests")
    class FindTests {

         @Test
        @DisplayName("Debe devolver venta cuando existe")
        void testFindById_Existe_DevuelveDTO() {

            // Arrange
            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            // Act
            VentaResponseDTO result =
                    ventaService.findById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(LocalDate.of(2025, 7, 6),
                    result.getFechaCompra());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando venta no existe")
        void testFindById_NoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(ventaRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.findById(99L));
        }

        @Test
        @DisplayName("Debe buscar ventas por método de pago")
        void testFindByMetodoPago_DevuelveLista() {

            // Arrange
            when(ventaRepository.findByMetodoPagoModel_Id(1L))
                    .thenReturn(List.of(ventaEntity()));

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findByMetodoPago(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Tarjeta de Crédito",
                    result.get(0).getMetodoPagoNombre());
        }

        @Test
        @DisplayName("Debe devolver lista vacía al buscar por método de pago")
        void testFindByMetodoPago_ListaVacia() {

            // Arrange
            when(ventaRepository.findByMetodoPagoModel_Id(1L))
                    .thenReturn(List.of());

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findByMetodoPago(1L);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Debe buscar ventas por fecha")
        void testFindByFechaCompra_DevuelveLista() {

            // Arrange
            LocalDate fecha = LocalDate.of(2025, 7, 6);

            when(ventaRepository.findByFechaCompra(fecha))
                    .thenReturn(List.of(ventaEntity()));

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findByFechaCompra(fecha);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(fecha,
                    result.get(0).getFechaCompra());
        }

        @Test
        @DisplayName("Debe buscar ventas por hora")
        void testFindByHoraCompra_DevuelveLista() {

            // Arrange
            LocalTime hora = LocalTime.of(14, 30);

            when(ventaRepository.findByHoraCompra(hora))
                    .thenReturn(List.of(ventaEntity()));

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findByHoraCompra(hora);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Debe buscar ventas por usuario y método de pago")
        void testFindByUsuarioYMetodoPago_DevuelveLista() {

            // Arrange
            when(ventaRepository
                    .findByUsuarioModel_IdAndMetodoPagoModel_Id(1L, 1L))
                    .thenReturn(List.of(ventaEntity()));

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findByUsuarioYMetodoPago(1L, 1L);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getUsuarioId());
            assertEquals(1L, result.get(0).getMetodoPagoId());
        }
    }
    
       

    // ===================== SAVE =====================
    @Nested 
    @DisplayName("Save Tests")
    class SaveTests {

         @Test
        @DisplayName("Debe guardar venta correctamente")
        void testSave_OK_GuardaVenta() {

            // Arrange
            VentaModel venta = ventaConCamposRequeridos();

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> {
                        VentaModel v = inv.getArgument(0);
                        v.setId(10L);
                        return v;
                    });

            // Act
            VentaResponseDTO result = ventaService.save(venta);

            // Assert
            assertNotNull(result);
            assertEquals(10L, result.getId());
            verify(ventaRepository, times(1))
                    .save(any(VentaModel.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando no existe usuario")
        void testSave_SinUsuario_LanzaRecursoNoEncontrado() {

            // Arrange
            VentaModel venta = new VentaModel();

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));

            verify(ventaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando no existe método de pago")
        void testSave_SinMetodoPago_LanzaRecursoNoEncontrado() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            venta.setUsuarioModel(usuario);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando no existe método de envío")
        void testSave_SinMetodoEnvio_LanzaRecursoNoEncontrado() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            MetodoPagoModel metodoPago = new MetodoPagoModel();
            metodoPago.setId(1L);

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(metodoPago);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando no existe estado")
        void testSave_SinEstado_LanzaRecursoNoEncontrado() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            MetodoPagoModel metodoPago = new MetodoPagoModel();
            metodoPago.setId(1L);

            MetodoEnvioModel metodoEnvio = new MetodoEnvioModel();
            metodoEnvio.setId(1L);

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(metodoPago);
            venta.setMetodoEnvioModel(metodoEnvio);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando usuario tiene ID nulo")
        void testSave_UsuarioConIdNull_LanzaExcepcion() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(null);

            venta.setUsuarioModel(usuario);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando método de pago tiene ID nulo")
        void testSave_MetodoPagoConIdNull_LanzaExcepcion() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            MetodoPagoModel metodoPago = new MetodoPagoModel();
            metodoPago.setId(null);

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(metodoPago);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando método de envío tiene ID nulo")
        void testSave_MetodoEnvioConIdNull_LanzaExcepcion() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            MetodoPagoModel metodoPago = new MetodoPagoModel();
            metodoPago.setId(1L);

            MetodoEnvioModel metodoEnvio = new MetodoEnvioModel();
            metodoEnvio.setId(null);

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(metodoPago);
            venta.setMetodoEnvioModel(metodoEnvio);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando estado tiene ID nulo")
        void testSave_EstadoConIdNull_LanzaExcepcion() {

            // Arrange
            VentaModel venta = new VentaModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(1L);

            MetodoPagoModel metodoPago = new MetodoPagoModel();
            metodoPago.setId(1L);

            MetodoEnvioModel metodoEnvio = new MetodoEnvioModel();
            metodoEnvio.setId(1L);

            EstadoModel estado = new EstadoModel();
            estado.setId(null);

            venta.setUsuarioModel(usuario);
            venta.setMetodoPagoModel(metodoPago);
            venta.setMetodoEnvioModel(metodoEnvio);
            venta.setEstado(estado);

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.save(venta));
        }

        @Test
        @DisplayName("Debe asignar fecha y hora automáticamente")
        void testSave_SinFecha_AsignaFechaActual() {

            // Arrange
            VentaModel venta = ventaConCamposRequeridos();

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> {
                        VentaModel v = inv.getArgument(0);

                        assertNotNull(v.getFechaCompra());
                        assertNotNull(v.getHoraCompra());

                        v.setId(5L);
                        return v;
                    });

            // Act
            VentaResponseDTO result = ventaService.save(venta);

            // Assert
            assertNotNull(result);
        }

        @Test
        @DisplayName("Debe conservar fecha y hora existentes")
        void testSave_ConFechaYHoraExistentes_NoLasReemplaza() {

            // Arrange
            VentaModel venta = ventaConCamposRequeridos();

            venta.setFechaCompra(LocalDate.of(2025, 12, 25));
            venta.setHoraCompra(LocalTime.of(18, 30));

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            // Act
            VentaResponseDTO result = ventaService.save(venta);

            // Assert
            assertEquals(LocalDate.of(2025, 12, 25),
                    result.getFechaCompra());

            assertEquals(LocalTime.of(18, 30),
                    result.getHoraCompra());
        }
    }


    // ===================== UPDATE =====================
    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar método de pago")
        void testUpdate_OK_CambiaMetodoPago() {

            // Arrange
            MetodoPagoModel nuevoMetodo = new MetodoPagoModel();
            nuevoMetodo.setId(2L);
            nuevoMetodo.setNombre("Transferencia");
            nuevoMetodo.setActivo(true);

            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            when(metodoPagoRepository.findById(2L))
                    .thenReturn(Optional.of(nuevoMetodo));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setMetodoPagoId(2L);

            // Act
            VentaResponseDTO result =
                    ventaService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals("Transferencia",
                    result.getMetodoPagoNombre());
        }

        @Test
        @DisplayName("Debe actualizar fecha y hora")
        void testUpdate_CambiaFechaYHora() {

            // Arrange
            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            LocalDate nuevaFecha = LocalDate.of(2025, 12, 25);
            LocalTime nuevaHora = LocalTime.of(18, 0);

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setFechaCompra(nuevaFecha);
            dto.setHoraCompra(nuevaHora);

            // Act
            VentaResponseDTO result =
                    ventaService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals(nuevaFecha,
                    result.getFechaCompra());

            assertEquals(nuevaHora,
                    result.getHoraCompra());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la venta no existe")
        void testUpdate_IdNoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(ventaRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.update(99L,
                            new VentaUpdateDTO()));

            verify(ventaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe actualizar método de envío")
        void testUpdate_CambiaMetodoEnvio() {

            // Arrange
            MetodoEnvioModel nuevoEnvio = new MetodoEnvioModel();
            nuevoEnvio.setId(2L);
            nuevoEnvio.setNombre("Retiro en tienda");

            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            when(metodoEnvioRepository.findById(2L))
                    .thenReturn(Optional.of(nuevoEnvio));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setMetodoEnvioId(2L);

            // Act
            VentaResponseDTO result =
                    ventaService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals("Retiro en tienda",
                    result.getMetodoEnvioNombre());
        }

        @Test
        @DisplayName("Debe actualizar estado")
        void testUpdate_CambiaEstado() {

            // Arrange
            EstadoModel nuevoEstado = new EstadoModel();
            nuevoEstado.setId(2L);
            nuevoEstado.setNombre("Enviada");

            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            when(estadoRepository.findById(2L))
                    .thenReturn(Optional.of(nuevoEstado));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setEstadoId(2L);

            // Act
            VentaResponseDTO result =
                    ventaService.update(1L, dto);

            // Assert
            assertNotNull(result);
            assertEquals("Enviada",
                    result.getEstadoNombre());
        }
    }
    // ===================== PATCH =====================

   @Nested
    @DisplayName("Patch Tests")
    class PatchTests {

        @Test
        @DisplayName("Debe modificar únicamente el estado")
        void testPatch_ModificaSoloEstado() {

            // Arrange
            EstadoModel nuevoEstado = new EstadoModel();
            nuevoEstado.setId(2L);
            nuevoEstado.setNombre("Enviada");
            nuevoEstado.setActivo(true);

            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(ventaEntity()));

            when(estadoRepository.findById(2L))
                    .thenReturn(Optional.of(nuevoEstado));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            VentaUpdateDTO dto = new VentaUpdateDTO();
            dto.setEstadoId(2L);

            // Act
            VentaResponseDTO result =
                    ventaService.patch(1L, dto);

            // Assert
            assertNotNull(result);

            assertEquals("Enviada",
                    result.getEstadoNombre());

            // Verifica que los demás datos no cambian
            assertEquals("Tarjeta de Crédito",
                    result.getMetodoPagoNombre());
        }
    }

    // ===================== DELETE =====================
    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar una venta existente")
        void testDeleteById_Existe_EliminaOK() {

            // Arrange
            when(ventaRepository.existsById(1L))
                    .thenReturn(true);

            // Act & Assert
            assertDoesNotThrow(() ->
                    ventaService.deleteById(1L));

            verify(ventaRepository, times(1))
                    .deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la venta no existe")
        void testDeleteById_NoExiste_LanzaRecursoNoEncontrado() {

            // Arrange
            when(ventaRepository.existsById(99L))
                    .thenReturn(false);

            // Act & Assert
            assertThrows(RecursoNoEncontradoException.class,
                    () -> ventaService.deleteById(99L));

            verify(ventaRepository, never())
                    .deleteById(anyLong());
        }
    }
    
    // ===================== CREAR VENTA DESDE REQUEST =====================

    @Nested
    @DisplayName("Crear Venta Tests")
    class CrearVentaTests {

        @Test
        @DisplayName("Debe crear una venta con detalles")
        void testCrearVentaDesdeRequest_OK_ConDetalles() {

            // Arrange
            ProductoModel producto = new ProductoModel();
            producto.setId(1L);
            producto.setNombre("The Witcher 3");

            DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
            detalle.setProductoId(1L);
            detalle.setCantidad(2);
            detalle.setPrecioUnitario(29990f);

            VentaRequestDTO request = new VentaRequestDTO();
            request.setMetodoPagoId(1L);
            request.setMetodoEnvioId(1L);
            request.setEstadoId(1L);
            request.setDetalles(List.of(detalle));

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(productoRepository.findById(1L))
                    .thenReturn(Optional.of(producto));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> {
                        VentaModel v = inv.getArgument(0);
                        v.setId(20L);
                        return v;
                    });

            // Act
            VentaResponseDTO result =
                    ventaService.crearVentaDesdeRequest(request, 1L);

            // Assert
            assertNotNull(result);
            assertEquals(20L, result.getId());

            verify(productoRepository, times(1))
                    .findById(1L);

            verify(ventaRepository, times(1))
                    .save(any(VentaModel.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando usuario no existe")
        void testCrearVentaDesdeRequest_UsuarioNoEncontrado_LanzaExcepcion() {

            // Arrange
            VentaRequestDTO request = new VentaRequestDTO();
            request.setMetodoPagoId(1L);
            request.setMetodoEnvioId(1L);
            request.setEstadoId(1L);

            when(usuarioRepository.findById(99L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(Exception.class,
                    () -> ventaService.crearVentaDesdeRequest(request, 99L));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando producto no existe")
        void testCrearVentaDesdeRequest_ProductoNoEncontrado_LanzaExcepcion() {

            // Arrange
            DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
            detalle.setProductoId(999L);
            detalle.setCantidad(1);
            detalle.setPrecioUnitario(9990f);

            VentaRequestDTO request = new VentaRequestDTO();
            request.setMetodoPagoId(1L);
            request.setMetodoEnvioId(1L);
            request.setEstadoId(1L);
            request.setDetalles(List.of(detalle));

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(productoRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(Exception.class,
                    () -> ventaService.crearVentaDesdeRequest(request, 1L));
        }

        @Test
        @DisplayName("Debe crear una venta sin detalles")
        void testCrearVentaDesdeRequest_SinDetalles_GuardaVentaVacia() {

            // Arrange
            VentaRequestDTO request = new VentaRequestDTO();
            request.setMetodoPagoId(1L);
            request.setMetodoEnvioId(1L);
            request.setEstadoId(1L);
            request.setDetalles(null);

            when(usuarioRepository.findById(1L))
                    .thenReturn(Optional.of(usuarioEntity()));

            when(metodoPagoRepository.findById(1L))
                    .thenReturn(Optional.of(metodoPagoEntity()));

            when(metodoEnvioRepository.findById(1L))
                    .thenReturn(Optional.of(metodoEnvioEntity()));

            when(estadoRepository.findById(1L))
                    .thenReturn(Optional.of(estadoEntity()));

            when(ventaRepository.save(any(VentaModel.class)))
                    .thenAnswer(inv -> {
                        VentaModel v = inv.getArgument(0);
                        v.setId(21L);
                        return v;
                    });

            // Act
            VentaResponseDTO result =
                    ventaService.crearVentaDesdeRequest(request, 1L);

            // Assert
            assertNotNull(result);
            assertEquals(21L, result.getId());

            verify(productoRepository, never())
                    .findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Resumen Tests")
    class ResumenTests {

        @Test
        @DisplayName("Debe obtener resumen de ventas")
        void testObtenerVentasConDatos_OK() {

            // Arrange
            Object[] fila = {
                    1L,
                    LocalDate.of(2025, 7, 6),
                    LocalTime.of(14, 30),
                    1L,
                    "Juan",
                    "Tarjeta de Crédito",
                    "Despacho a domicilio",
                    "Pagada"
            };

            List<Object[]> filas = new ArrayList<>();
            filas.add(fila);

            when(ventaRepository.obtenerVentasResumen())
                    .thenReturn(filas);

            // Act
            List<Map<String, Object>> result =
                    ventaService.obtenerVentasConDatos();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).get("ID"));
        }

        @Test
        @DisplayName("Debe devolver lista vacía cuando no existen ventas")
        void testObtenerVentasConDatos_Vacio() {

            // Arrange
            when(ventaRepository.obtenerVentasResumen())
                    .thenReturn(List.of());

            // Act
            List<Map<String, Object>> result =
                    ventaService.obtenerVentasConDatos();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Pagination And Mapper Tests")
    class PaginationAndMapperTests {

        @Test
        @DisplayName("Debe obtener ventas paginadas")
        void testFindAllPaged_OK() {

            // Arrange
            Page<VentaModel> page =
                    new PageImpl<>(List.of(ventaEntity()));

            when(ventaRepository.findAll(
                    any(org.springframework.data.domain.Pageable.class)))
                    .thenReturn(page);

            // Act
            PagedResponse<VentaResponseDTO> result =
                    ventaService.findAllPaged(1, 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getContenido().size());
            assertEquals(1L,
                    result.getContenido().get(0).getId());
        }

        @Test
        @DisplayName("Debe obtener compras paginadas de un usuario")
        void testFindMisComprasPaged_OK() {

            // Arrange
            Page<VentaModel> page =
                    new PageImpl<>(List.of(ventaEntity()));

            when(ventaRepository.findByUsuarioModel_Id(
                    eq(1L),
                    any(org.springframework.data.domain.Pageable.class)))
                    .thenReturn(page);

            // Act
            PagedResponse<VentaResponseDTO> result =
                    ventaService.findMisComprasPaged(1L, 1, 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getContenido().size());
            assertEquals(1L,
                    result.getContenido().get(0).getId());
        }

        @Test
        @DisplayName("Debe mapear venta sin relaciones")
        void testFindById_VentaSinRelaciones_CubreToResponseDTO() {

            // Arrange
            VentaModel venta = new VentaModel();
            venta.setId(50L);

            when(ventaRepository.findById(50L))
                    .thenReturn(Optional.of(venta));

            // Act
            VentaResponseDTO result =
                    ventaService.findById(50L);

            // Assert
            assertNotNull(result);
            assertEquals(50L, result.getId());
        }

        @Test
        @DisplayName("Debe mapear detalle sin producto")
        void testFindById_DetalleSinProducto_CubreToDetalleResponseDTO() {

            // Arrange
            VentaModel venta = ventaEntity();

            DetalleVentaModel detalle =
                    new DetalleVentaModel();

            detalle.setId(1L);
            detalle.setCantidad(2);
            detalle.setPrecioUnitario(1000f);

                venta.setDetalles(List.of(detalle));

            when(ventaRepository.findById(1L))
                    .thenReturn(Optional.of(venta));

            // Act
            VentaResponseDTO result =
                    ventaService.findById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1,
                    result.getDetalles().size());
        }

        @Test
        @DisplayName("Debe mapear venta sin detalles")
        void testFindAll_VentaSinDetalles_CubreMapper() {

            // Arrange
            VentaModel venta = ventaEntity();
            venta.setDetalles(null);

            when(ventaRepository.findAll())
                    .thenReturn(List.of(venta));

            // Act
            List<VentaResponseDTO> result =
                    ventaService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

}