package com.example.NoLimits.Multimedia;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.model.*;
import com.example.NoLimits.Multimedia.repository.*;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private EstadoRepository estadoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ImagenesRepository imagenesRepository;

    // Géneros (TNP + TP)
    @Autowired private GeneroRepository generoRepository;
    @Autowired private GenerosRepository generosRepository;

    // Desarrolladores (TNP + TP)
    @Autowired private DesarrolladorRepository desarrolladorRepository;
    @Autowired private DesarrolladoresRepository desarrolladoresRepository;

    // Tipo de desarrollador (TNP + TP)
    @Autowired private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;
    @Autowired private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        // ================== TIPOS DE PRODUCTO ==================
        if (tipoProductoRepository.count() == 0) {
            String[][] tiposBase = {
                    { "Película", "Productos de tipo película (Blu-ray, digital, etc.)" },
                    { "Videojuego", "Productos de tipo videojuego (consolas, PC, etc.)" },
                    { "Accesorio", "Accesorios relacionados (controles, headsets, etc.)" }
            };

            for (String[] t : tiposBase) {
                String nombre = t[0], descripcion = t[1];

                if (tipoProductoRepository.existsByNombreIgnoreCase(nombre)) continue;

                TipoProductoModel tipo = new TipoProductoModel();
                tipo.setNombre(nombre.trim());
                tipo.setDescripcion(descripcion.length() > 255 ? descripcion.substring(0, 255) : descripcion);
                tipo.setActivo(true);
                tipoProductoRepository.save(tipo);
            }
        }

        List<TipoProductoModel> tiposProducto = tipoProductoRepository.findAll();
        if (tiposProducto.isEmpty()) return;

        // ================== ESTADOS ==================
        if (estadoRepository.count() == 0) {
            String[] estadosBase = { "Activo", "Agotado", "Descontinuado" };
            for (String nombre : estadosBase) {
                if (estadoRepository.existsByNombreIgnoreCase(nombre)) continue;

                EstadoModel e = new EstadoModel();
                e.setNombre(nombre.trim());
                e.setDescripcion(null);
                e.setActivo(true);
                estadoRepository.save(e);
            }
        }

        List<EstadoModel> estados = estadoRepository.findAll();
        if (estados.isEmpty()) return;

        // ================== CLASIFICACIONES ==================
        if (clasificacionRepository.count() == 0) {
            Object[][] base = {
                    { "E", "Para todo público." },
                    { "T", "Contenido apto para adolescentes." },
                    { "+13", "Contenido recomendado para mayores de 13 años." },
                    { "+16", "Contenido recomendado para mayores de 16 años." },
                    { "+18", "Solo para adultos." }
            };

            for (Object[] c : base) {
                String nombre = (String) c[0], descripcion = (String) c[1];

                if (clasificacionRepository.existsByNombreIgnoreCase(nombre)) continue;

                ClasificacionModel cl = new ClasificacionModel();
                cl.setNombre(nombre.trim());
                cl.setDescripcion(descripcion.length() > 255 ? descripcion.substring(0, 255) : descripcion);
                cl.setActivo(true);
                clasificacionRepository.save(cl);
            }
        }

        List<ClasificacionModel> clasificaciones = clasificacionRepository.findAll();
        if (clasificaciones.isEmpty()) return;

        // ================== GÉNEROS (TNP) ==================
        if (generoRepository.count() == 0) {
            String[] generosBase = {
                    "Acción", "Aventura", "Terror", "Romance", "Puzzle",
                    "Comedia", "Drama", "Sci-Fi", "RPG", "Shooter"
            };

            for (String g : generosBase) {
                if (generoRepository.existsByNombreIgnoreCase(g)) continue;

                GeneroModel genero = new GeneroModel();
                genero.setNombre(g.trim());
                generoRepository.save(genero);
            }
        }

        List<GeneroModel> generos = generoRepository.findAll();
        if (generos.isEmpty()) return;

        // ================== DESARROLLADORES (TNP) ==================
        if (desarrolladorRepository.count() == 0) {
            String[] devsBase = {
                    "Insomniac Games",
                    "FromSoftware",
                    "CD Projekt RED",
                    "Naughty Dog",
                    "Santa Monica Studio",
                    "Valve",
                    "Epic Games",
                    "Capcom",
                    "Square Enix",
                    "Rockstar Games"
            };

            for (String nombre : devsBase) {
                if (desarrolladorRepository.existsByNombreIgnoreCase(nombre)) continue;

                DesarrolladorModel dev = new DesarrolladorModel();
                dev.setNombre(nombre.trim());
                desarrolladorRepository.save(dev);
            }
        }

        List<DesarrolladorModel> desarrolladores = desarrolladorRepository.findAll();

        // ================== TIPOS DE DESARROLLADOR (TNP) ==================
        if (tipoDeDesarrolladorRepository.count() == 0) {
            String[] tiposDevBase = {
                    "Estudio",
                    "Publisher",
                    "Lead",
                    "Co-desarrollador",
                    "Freelancer"
            };

            for (String nombre : tiposDevBase) {
                if (tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(nombre)) continue;

                TipoDeDesarrolladorModel tipoDev = new TipoDeDesarrolladorModel();
                tipoDev.setNombre(nombre.trim());
                tipoDeDesarrolladorRepository.save(tipoDev);
            }
        }

        List<TipoDeDesarrolladorModel> tiposDesarrollador = tipoDeDesarrolladorRepository.findAll();

        // ================== PRODUCTOS ==================
        for (int i = 0; i < 30; i++) {

            String nombre = faker.commerce().productName();
            if (nombre.length() > 100) nombre = nombre.substring(0, 100);
            if (productoRepository.existsByNombreIgnoreCase(nombre)) continue;

            ProductoModel p = new ProductoModel();
            p.setNombre(nombre.trim());

            double precio = faker.number().randomDouble(0, 5_000, 50_000);
            p.setPrecio(precio <= 0 ? 9_990 : precio);

            p.setTipoProducto(tiposProducto.get(random.nextInt(tiposProducto.size())));
            p.setEstado(estados.get(random.nextInt(estados.size())));
            p.setClasificacion(clasificaciones.get(random.nextInt(clasificaciones.size())));

            productoRepository.save(p);
        }

        List<ProductoModel> productos = productoRepository.findAll();
        if (productos.isEmpty()) return;

        // ================== ASIGNAR GÉNEROS (TP) ==================
        for (ProductoModel producto : productos) {

            int cantidadGeneros = 1 + random.nextInt(3);
            for (int i = 0; i < cantidadGeneros; i++) {

                GeneroModel generoRandom = generos.get(random.nextInt(generos.size()));

                if (!generosRepository.existsByProducto_IdAndGenero_Id(producto.getId(), generoRandom.getId())) {
                    GenerosModel rel = new GenerosModel();
                    rel.setProducto(producto);
                    rel.setGenero(generoRandom);
                    generosRepository.save(rel);
                }
            }
        }

        // ================== ASIGNAR DESARROLLADORES A PRODUCTOS (TP) ==================
        if (!desarrolladores.isEmpty()) {
            for (ProductoModel producto : productos) {

                int cantidadDevs = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadDevs; i++) {
                    DesarrolladorModel devRandom = desarrolladores.get(random.nextInt(desarrolladores.size()));

                    if (!desarrolladoresRepository.existsByProducto_IdAndDesarrollador_Id(producto.getId(), devRandom.getId())) {
                        DesarrolladoresModel rel = new DesarrolladoresModel();
                        rel.setProducto(producto);
                        rel.setDesarrollador(devRandom);
                        desarrolladoresRepository.save(rel);
                    }
                }
            }
        }

        // ================== ASIGNAR TIPOS A DESARROLLADORES (TP) ==================
        if (!desarrolladores.isEmpty() && !tiposDesarrollador.isEmpty()) {
            for (DesarrolladorModel dev : desarrolladores) {
                int cantidadTipos = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadTipos; i++) {
                    TipoDeDesarrolladorModel tipoRandom =
                            tiposDesarrollador.get(random.nextInt(tiposDesarrollador.size()));

                    if (!tiposDeDesarrolladorRepository
                            .existsByDesarrollador_IdAndTipoDeDesarrollador_Id(dev.getId(), tipoRandom.getId())) {

                        TiposDeDesarrolladorModel link = new TiposDeDesarrolladorModel();
                        link.setDesarrollador(dev);
                        link.setTipoDeDesarrollador(tipoRandom);
                        tiposDeDesarrolladorRepository.save(link);
                    }
                }
            }
        }

        // ================== IMÁGENES ==================
        for (ProductoModel producto : productos) {

            int cantidadImagenes = 1 + random.nextInt(3);

            for (int i = 0; i < cantidadImagenes; i++) {
                ImagenesModel img = new ImagenesModel();

                img.setRuta("/assets/img/productos/" + producto.getId() + "_" + (i + 1) + ".webp");

                String alt = "Imagen " + (i + 1) + " de " + producto.getNombre();
                img.setAltText(alt.length() > 150 ? alt.substring(0, 150) : alt);

                img.setProducto(producto);

                imagenesRepository.save(img);
            }
        }

        System.out.println(
            "[DataLoader] Seed dev completo: tipos, estados, clasificaciones, géneros, " +
            "desarrolladores, tipos de desarrollador, relaciones TP, productos e imágenes."
        );
    }
}