
package com.example.NoLimits.assemblers.catalogos;

import com.example.NoLimits.Multimedia.assemblers.catalogos.EmpresasModelAssembler;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmpresasModelAssembler Tests")
class EmpresasModelAssemblerTest {

    private final EmpresasModelAssembler assembler =
            new EmpresasModelAssembler();

    @Test
    @DisplayName("Debe crear EntityModel con links")
    void debeCrearEntityModelConLinks() {

        EmpresasResponseDTO dto = new EmpresasResponseDTO();
        dto.setProductoId(1L);
        dto.setEmpresaId(2L);

        EntityModel<EmpresasResponseDTO> model =
                assembler.toModel(dto);

        assertNotNull(model);
        assertEquals(dto, model.getContent());

        assertTrue(model.hasLink("producto-empresas"));
        assertTrue(model.hasLink("desvincular"));
    }
}