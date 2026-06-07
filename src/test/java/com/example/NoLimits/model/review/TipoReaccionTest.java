package com.example.NoLimits.model.review;

import com.example.NoLimits.Multimedia.model.review.TipoReaccion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoReaccion Tests")
class TipoReaccionTest {

    @Test
    @DisplayName("debe contener los valores esperados")
    void debeContenerValoresEsperados() {

        TipoReaccion[] valores = TipoReaccion.values();

        assertEquals(2, valores.length);
        assertEquals(TipoReaccion.LIKE, valores[0]);
        assertEquals(TipoReaccion.DISLIKE, valores[1]);
    }

    @Test
    @DisplayName("valueOf debe retornar LIKE")
    void valueOfLike() {

        assertEquals(
                TipoReaccion.LIKE,
                TipoReaccion.valueOf("LIKE")
        );
    }

    @Test
    @DisplayName("valueOf debe retornar DISLIKE")
    void valueOfDislike() {

        assertEquals(
                TipoReaccion.DISLIKE,
                TipoReaccion.valueOf("DISLIKE")
        );
    }
}