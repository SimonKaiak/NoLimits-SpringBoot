package com.example.NoLimits.Multimedia.service.producto;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioSchedulerService {

    private final ProductoService productoService;

    public PrecioSchedulerService(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Se ejecuta todos los días a las 3 AM
    @Scheduled(cron = "0 0 3 * * *")
    public void actualizarPreciosSteamDiariamente() {

        List<Long> ids = productoService.obtenerIdsProductosConAppId();

        for (Long id : ids) {
            try {
                productoService.actualizarPrecioDesdeSteam(id);
            } catch (Exception e) {
                System.out.println("Error actualizando producto ID " + id + ": " + e.getMessage());
            }
        }
    }
}