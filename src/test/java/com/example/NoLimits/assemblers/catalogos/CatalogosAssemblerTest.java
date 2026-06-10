package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.GeneroModelAssembler;
import com.example.NoLimits.Multimedia.assemblers.catalogos.TipoProductoModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoProductoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Catalogos Assemblers")
class CatalogosAssemblerTest {

    @Nested
    @DisplayName("GeneroModelAssembler")
    class GeneroAssemblerTests {

        private final GeneroModelAssembler assembler = new GeneroModelAssembler();

        @Test
        @DisplayName("crea EntityModel con todos los links cuando id no es null")
        void creaEntityModelConLinks() {
            GeneroResponseDTO dto = new GeneroResponseDTO();
            dto.setId(1L);
            dto.setNombre("Acción");

            EntityModel<GeneroResponseDTO> model = assembler.toModel(dto);

            assertNotNull(model);
            assertEquals(dto, model.getContent());
            assertTrue(model.hasLink("self"));
            assertTrue(model.hasLink("actualizar"));
            assertTrue(model.hasLink("actualizar_parcial"));
            assertTrue(model.hasLink("eliminar"));
            assertTrue(model.hasLink("generos"));
            assertTrue(model.hasLink("crear"));
        }

        @Test
        @DisplayName("crea EntityModel con id distinto")
        void creaEntityModelConIdDistinto() {
            GeneroResponseDTO dto = new GeneroResponseDTO();
            dto.setId(99L);
            dto.setNombre("Terror");

            EntityModel<GeneroResponseDTO> model = assembler.toModel(dto);

            assertNotNull(model);
            assertEquals("Terror", model.getContent().getNombre());
        }
    }

    @Nested
    @DisplayName("TipoProductoModelAssembler")
    class TipoProductoAssemblerTests {

        private final TipoProductoModelAssembler assembler = new TipoProductoModelAssembler();

        @Test
        @DisplayName("crea EntityModel con links cuando id no es null")
        void creaEntityModelConIdNoNull() {
            TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
            dto.setId(1L);
            dto.setNombre("Videojuego");

            EntityModel<TipoProductoResponseDTO> model = assembler.toModel(dto);

            assertNotNull(model);
            assertTrue(model.hasLink("self"));
            assertTrue(model.hasLink("tipos_producto"));
            assertTrue(model.hasLink("crear"));
            assertTrue(model.hasLink("actualizar"));
            assertTrue(model.hasLink("actualizar_parcial"));
            assertTrue(model.hasLink("eliminar"));
        }

        @Test
        @DisplayName("crea EntityModel sin links de id cuando id es null — cubre rama if(id != null)")
        void creaEntityModelSinLinksDeIdCuandoIdEsNull() {
            TipoProductoResponseDTO dto = new TipoProductoResponseDTO();
            dto.setId(null);
            dto.setNombre("Sin ID");

            EntityModel<TipoProductoResponseDTO> model = assembler.toModel(dto);

            assertNotNull(model);
            assertFalse(model.hasLink("self"));
            assertFalse(model.hasLink("actualizar"));
            assertTrue(model.hasLink("tipos_producto"));
            assertTrue(model.hasLink("crear"));
        }
    }
}