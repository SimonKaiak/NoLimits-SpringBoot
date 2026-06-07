package com.example.NoLimits.dto.producto.mapper;

import com.example.NoLimits.Multimedia.dto.producto.mapper.ProductoMapper;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.model.catalogos.*;
import com.example.NoLimits.Multimedia.model.producto.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoMapperTest {

    @Nested
    @DisplayName("Mapeo a ProductoResponseDTO")
    class MapeoProductoResponseDTO {

        @Test
        @DisplayName("convierte producto con relaciones nulas")
        void testToResponseDTOConRelacionesNulas() {

            ProductoModel model = mock(ProductoModel.class);

            when(model.getId()).thenReturn(10L);
            when(model.getNombre()).thenReturn("Spider-Man");
            when(model.getPrecio()).thenReturn(12990.0);
            when(model.getSinopsis()).thenReturn("Sinopsis");
            when(model.getUrlTrailer()).thenReturn("https://youtube.com/test");
            when(model.getAnio()).thenReturn(2002);
            when(model.getSaga()).thenReturn("Spider-Man");
            when(model.getPortadaSaga()).thenReturn("/sagas/spiderman.webp");

            when(model.getTipoProducto()).thenReturn(null);
            when(model.getClasificacion()).thenReturn(null);
            when(model.getEstado()).thenReturn(null);
            when(model.getTipoEmpresa()).thenReturn(null);
            when(model.getTipoDesarrollador()).thenReturn(null);

            when(model.getPlataformas()).thenReturn(null);
            when(model.getGeneros()).thenReturn(null);
            when(model.getEmpresas()).thenReturn(null);
            when(model.getDesarrolladores()).thenReturn(null);
            when(model.getImagenes()).thenReturn(null);
            when(model.getLinksCompra()).thenReturn(null);

            ProductoResponseDTO dto = ProductoMapper.toResponseDTO(model);

            assertNotNull(dto);
            assertEquals(10L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("Sinopsis", dto.getSinopsis());
            assertEquals("https://youtube.com/test", dto.getUrlTrailer());
            assertEquals(2002, dto.getAnio());
            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/sagas/spiderman.webp", dto.getPortadaSaga());

            assertNull(dto.getTipoProductoId());
            assertNull(dto.getClasificacionId());
            assertNull(dto.getEstadoId());
            assertNull(dto.getTipoEmpresaId());
            assertNull(dto.getTipoDesarrolladorId());

            assertNull(dto.getPlataformas());
            assertNull(dto.getGeneros());
            assertNull(dto.getEmpresas());
            assertNull(dto.getDesarrolladores());
            assertNull(dto.getImagenes());

            assertNotNull(dto.getLinksCompra());
            assertTrue(dto.getLinksCompra().isEmpty());
        }

        @Test
        @DisplayName("convierte producto con relaciones completas")
        void testToResponseDTOConRelacionesCompletas() {

            ProductoModel model = mock(ProductoModel.class);

            TipoProductoModel tipoProducto = mock(TipoProductoModel.class);
            ClasificacionModel clasificacion = mock(ClasificacionModel.class);
            EstadoModel estado = mock(EstadoModel.class);
            TipoEmpresaModel tipoEmpresa = mock(TipoEmpresaModel.class);
            TipoDeDesarrolladorModel tipoDesarrollador = mock(TipoDeDesarrolladorModel.class);

            when(tipoProducto.getId()).thenReturn(1L);
            when(tipoProducto.getNombre()).thenReturn("VIDEOJUEGO");

            when(clasificacion.getId()).thenReturn(2L);
            when(clasificacion.getNombre()).thenReturn("TE+7");

            when(estado.getId()).thenReturn(3L);
            when(estado.getNombre()).thenReturn("Activo");

            when(tipoEmpresa.getId()).thenReturn(4L);
            when(tipoEmpresa.getNombre()).thenReturn("Publisher");

            when(tipoDesarrollador.getId()).thenReturn(5L);
            when(tipoDesarrollador.getNombre()).thenReturn("Estudio");

            PlataformasModel plataformaRelacion = mock(PlataformasModel.class);
            PlataformaModel plataforma = mock(PlataformaModel.class);
            when(plataforma.getId()).thenReturn(10L);
            when(plataforma.getNombre()).thenReturn("Steam");
            when(plataformaRelacion.getPlataforma()).thenReturn(plataforma);

            GenerosModel generoRelacion = mock(GenerosModel.class);
            GeneroModel genero = mock(GeneroModel.class);
            when(genero.getNombre()).thenReturn("Acción");
            when(generoRelacion.getGenero()).thenReturn(genero);

            EmpresasModel empresaRelacion = mock(EmpresasModel.class);
            EmpresaModel empresa = mock(EmpresaModel.class);
            when(empresa.getNombre()).thenReturn("Sony");
            when(empresaRelacion.getEmpresa()).thenReturn(empresa);

            DesarrolladoresModel desarrolladorRelacion = mock(DesarrolladoresModel.class);
            DesarrolladorModel desarrollador = mock(DesarrolladorModel.class);
            when(desarrollador.getNombre()).thenReturn("Insomniac");
            when(desarrolladorRelacion.getDesarrollador()).thenReturn(desarrollador);

            ImagenesModel imagen = mock(ImagenesModel.class);
            when(imagen.getRuta()).thenReturn("/img/spiderman.webp");

            ProductoLinkCompraModel link = mock(ProductoLinkCompraModel.class);
            when(link.getPlataforma()).thenReturn(plataforma);
            when(link.getUrl()).thenReturn("https://store.steampowered.com/app/test");
            when(link.getLabel()).thenReturn("Ver en Steam");
            when(link.getAppId()).thenReturn("12345");
            when(link.getPrecioActual()).thenReturn(19990.0);

            when(model.getId()).thenReturn(100L);
            when(model.getNombre()).thenReturn("Spider-Man");
            when(model.getPrecio()).thenReturn(12990.0);
            when(model.getSinopsis()).thenReturn("Sinopsis");
            when(model.getUrlTrailer()).thenReturn("https://youtube.com/test");
            when(model.getAnio()).thenReturn(2011);
            when(model.getSaga()).thenReturn("Spider-Man");
            when(model.getPortadaSaga()).thenReturn("/sagas/spiderman.webp");

            when(model.getTipoProducto()).thenReturn(tipoProducto);
            when(model.getClasificacion()).thenReturn(clasificacion);
            when(model.getEstado()).thenReturn(estado);
            when(model.getTipoEmpresa()).thenReturn(tipoEmpresa);
            when(model.getTipoDesarrollador()).thenReturn(tipoDesarrollador);

            when(model.getPlataformas()).thenReturn(Set.of(plataformaRelacion));
            when(model.getGeneros()).thenReturn(Set.of(generoRelacion));
            when(model.getEmpresas()).thenReturn(Set.of(empresaRelacion));
            when(model.getDesarrolladores()).thenReturn(Set.of(desarrolladorRelacion));
            when(model.getImagenes()).thenReturn(List.of(imagen));
            when(model.getLinksCompra()).thenReturn(Set.of(link));

            ProductoResponseDTO dto = ProductoMapper.toResponseDTO(model);

            assertEquals(100L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("Sinopsis", dto.getSinopsis());
            assertEquals("https://youtube.com/test", dto.getUrlTrailer());
            assertEquals(2011, dto.getAnio());

            assertEquals(1L, dto.getTipoProductoId());
            assertEquals("VIDEOJUEGO", dto.getTipoProductoNombre());

            assertEquals(2L, dto.getClasificacionId());
            assertEquals("TE+7", dto.getClasificacionNombre());

            assertEquals(3L, dto.getEstadoId());
            assertEquals("Activo", dto.getEstadoNombre());

            assertEquals(4L, dto.getTipoEmpresaId());
            assertEquals("Publisher", dto.getTipoEmpresaNombre());

            assertEquals(5L, dto.getTipoDesarrolladorId());
            assertEquals("Estudio", dto.getTipoDesarrolladorNombre());

            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/sagas/spiderman.webp", dto.getPortadaSaga());

            assertEquals(1, dto.getPlataformas().size());
            assertEquals(10L, dto.getPlataformas().get(0).getId());
            assertEquals("Steam", dto.getPlataformas().get(0).getNombre());

            assertEquals(List.of("Acción"), dto.getGeneros());
            assertEquals(List.of("Sony"), dto.getEmpresas());
            assertEquals(List.of("Insomniac"), dto.getDesarrolladores());
            assertEquals(List.of("/img/spiderman.webp"), dto.getImagenes());

            assertEquals(1, dto.getLinksCompra().size());
            assertEquals(10L, dto.getLinksCompra().get(0).getPlataformaId());
            assertEquals("https://store.steampowered.com/app/test", dto.getLinksCompra().get(0).getUrl());
            assertEquals("Ver en Steam", dto.getLinksCompra().get(0).getLabel());
            assertEquals("12345", dto.getLinksCompra().get(0).getAppId());
            assertEquals(19990.0, dto.getLinksCompra().get(0).getPrecioActual());
        }
    }
}