// Ruta: src/main/java/com/example/NoLimits/Multimedia/DataLoader.java
package com.example.NoLimits.Multimedia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TiposEmpresaModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladoresRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.PlataformasRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ImagenesRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.RegionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

import net.datafaker.Faker;

/**
 * Cargador de datos de desarrollo (semilla).
 *
 * <p>
 * Esta clase se ejecuta automáticamente al iniciar la aplicación
 * cuando el perfil activo es {@code dev}. Su objetivo es poblar la base
 * de datos con datos iniciales coherentes para:
 * <ul>
 *   <li>Catálogo de productos (películas, videojuegos, accesorios).</li>
 *   <li>Tablas de apoyo (clasificaciones, géneros, plataformas, empresas, etc.).</li>
 *   <li>Usuarios de prueba con direcciones.</li>
 *   <li>Ventas y detalles de venta simulados.</li>
 *   <li>Imágenes asociadas a los productos.</li>
 * </ul>
 * </p>
 *
 * <p>
 * El uso de {@link Faker} y {@link Random} permite generar datos pseudo-aleatorios,
 * suficientes para pruebas de front y back sin intervención manual.
 * </p>
 */
@Profile("dev") // Solo se ejecuta en el perfil "dev", no en producción.
@Component
public class DataLoader implements CommandLineRunner {

    // ================== Repositorios para Tipos / Catálogos ==================

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenesRepository imagenesRepository;

    // Géneros (TNP + TP)
    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private GenerosRepository generosRepository;

    // Desarrolladores (TNP + TP)
    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    @Autowired
    private DesarrolladoresRepository desarrolladoresRepository;

    // Tipo de desarrollador (TNP + TP)
    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @Autowired
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    // Empresas (TNP + TP)
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresasRepository empresasRepository;

    // Tipo de empresa (TNP + TP)
    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    @Autowired
    private TiposEmpresaRepository tiposEmpresaRepository;

    // Plataformas (TNP + TP)
    @Autowired
    private PlataformaRepository plataformaRepository;

    @Autowired
    private PlataformasRepository plataformasRepository;

    // Nuevos: usuarios, métodos de pago y envío
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;

    // Nuevos: Rol, Región, Comuna, Dirección
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    // Nuevos: Ventas y Detalles
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    /**
     * Punto de entrada del cargador de datos.
     */
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
                String nombre = t[0];
                String descripcion = t[1];

                if (tipoProductoRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                TipoProductoModel tipo = new TipoProductoModel();
                tipo.setNombre(nombre.trim());
                tipo.setDescripcion(descripcion.length() > 255 ? descripcion.substring(0, 255) : descripcion);
                tipo.setActivo(true);
                tipoProductoRepository.save(tipo);
            }
        }

        List<TipoProductoModel> tiposProducto = tipoProductoRepository.findAll();
        if (tiposProducto.isEmpty()) {
            return;
        }

        // ================== ESTADOS ==================
        if (estadoRepository.count() == 0) {
            String[] estadosBase = { "Activo", "Agotado", "Descontinuado" };
            for (String nombre : estadosBase) {
                if (estadoRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                EstadoModel e = new EstadoModel();
                e.setNombre(nombre.trim());
                e.setDescripcion(null);
                e.setActivo(true);
                estadoRepository.save(e);
            }
        }

        List<EstadoModel> estados = estadoRepository.findAll();
        if (estados.isEmpty()) {
            return;
        }

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
                String nombre = (String) c[0];
                String descripcion = (String) c[1];

                if (clasificacionRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                ClasificacionModel cl = new ClasificacionModel();
                cl.setNombre(nombre.trim());
                cl.setDescripcion(descripcion.length() > 255 ? descripcion.substring(0, 255) : descripcion);
                cl.setActivo(true);
                clasificacionRepository.save(cl);
            }
        }

        List<ClasificacionModel> clasificaciones = clasificacionRepository.findAll();
        if (clasificaciones.isEmpty()) {
            return;
        }

        // ================== GÉNEROS (TNP) ==================
        if (generoRepository.count() == 0) {
            String[] generosBase = {
                    "Acción",
                    "Aventura",
                    "Terror",
                    "Romance",
                    "Puzzle",
                    "Comedia",
                    "Drama",
                    "Sci-Fi",
                    "RPG",
                    "Shooter"
            };

            for (String g : generosBase) {
                if (generoRepository.existsByNombreIgnoreCase(g)) {
                    continue;
                }

                GeneroModel genero = new GeneroModel();
                genero.setNombre(g.trim());
                generoRepository.save(genero);
            }
        }

        List<GeneroModel> generos = generoRepository.findAll();
        if (generos.isEmpty()) {
            return;
        }

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
                if (desarrolladorRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

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
                if (tipoDeDesarrolladorRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                TipoDeDesarrolladorModel tipoDev = new TipoDeDesarrolladorModel();
                tipoDev.setNombre(nombre.trim());
                tipoDeDesarrolladorRepository.save(tipoDev);
            }
        }

        List<TipoDeDesarrolladorModel> tiposDesarrollador = tipoDeDesarrolladorRepository.findAll();

        // ================== EMPRESAS (TNP) ==================
        if (empresaRepository.count() == 0) {
            String[] empresasBase = {
                    "Sony Pictures",
                    "Marvel Studios",
                    "Warner Bros",
                    "Paramount Pictures",
                    "Universal Pictures",
                    "Netflix Studios",
                    "Bandai Namco",
                    "Ubisoft",
                    "Electronic Arts",
                    "Bethesda Softworks"
            };

            for (String nombre : empresasBase) {
                if (empresaRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                EmpresaModel empresa = new EmpresaModel();
                empresa.setNombre(nombre.trim());
                empresaRepository.save(empresa);
            }
        }

        List<EmpresaModel> empresas = empresaRepository.findAll();

        // ================== TIPOS DE EMPRESA (TNP) ==================
        if (tipoEmpresaRepository.count() == 0) {
            String[] tiposEmpresaBase = {
                    "Publisher",
                    "Desarrolladora",
                    "Distribuidora",
                    "Estudio cinematográfico",
                    "Productora independiente"
            };

            for (String nombre : tiposEmpresaBase) {
                if (tipoEmpresaRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                TipoEmpresaModel tipoEmpresa = new TipoEmpresaModel();
                tipoEmpresa.setNombre(nombre.trim());
                tipoEmpresaRepository.save(tipoEmpresa);
            }
        }

        List<TipoEmpresaModel> tiposEmpresa = tipoEmpresaRepository.findAll();

        // ================== PLATAFORMAS (TNP) ==================
        if (plataformaRepository.count() == 0) {
            String[] plataformasBase = {
                    "PC",
                    "PlayStation 5",
                    "PlayStation 4",
                    "Xbox Series",
                    "Xbox One",
                    "Nintendo Switch",
                    "Steam",
                    "Epic Games Store"
            };

            for (String nombre : plataformasBase) {
                if (plataformaRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                PlataformaModel plataforma = new PlataformaModel();
                plataforma.setNombre(nombre.trim());
                plataformaRepository.save(plataforma);
            }
        }

        List<PlataformaModel> plataformas = plataformaRepository.findAll();

        // ================== MÉTODOS DE PAGO (TNP) ==================
        if (metodoPagoRepository.count() == 0) {
            String[] metodosPagoBase = {
                    "Tarjeta de Crédito",
                    "Tarjeta de Débito",
                    "Transferencia Bancaria",
                    "PayPal"
            };

            for (String nombre : metodosPagoBase) {
                if (metodoPagoRepository.existsByNombreIgnoreCase(nombre)) {
                    continue;
                }

                MetodoPagoModel mp = new MetodoPagoModel();
                mp.setNombre(nombre.trim());
                mp.setActivo(true);
                metodoPagoRepository.save(mp);
            }
        }

        List<MetodoPagoModel> metodosPago = metodoPagoRepository.findAll();

        // ================== MÉTODOS DE ENVÍO (TNP) ==================
        if (metodoEnvioRepository.count() == 0) {
            String[] metodosEnvioBase = {
                    "Retiro en tienda",
                    "Despacho a domicilio",
                    "Envío express"
            };

            for (String nombre : metodosEnvioBase) {
                MetodoEnvioModel me = new MetodoEnvioModel();
                me.setNombre(nombre.trim());
                me.setActivo(true);
                metodoEnvioRepository.save(me);
            }
        }

        List<MetodoEnvioModel> metodosEnvio = metodoEnvioRepository.findAll();

        // ================== ROLES ==================
        if (rolRepository.count() == 0) {
            String[] rolesBase = { "ADMIN", "CLIENTE", "VENDEDOR" };

            for (String nombre : rolesBase) {
                RolModel rol = new RolModel();
                rol.setNombre(nombre.trim());
                rol.setDescripcion("Rol generado automáticamente");
                rol.setActivo(true);
                rolRepository.save(rol);
            }
        }

        List<RolModel> roles = rolRepository.findAll();
        RolModel rolCliente = roles.stream()
                .filter(r -> r.getNombre().equalsIgnoreCase("CLIENTE"))
                .findFirst()
                .orElse(roles.get(0));

        // ================== REGIONES ==================
        if (regionRepository.count() == 0) {
            String[] regionesBase = {
                    "Región Metropolitana",
                    "Valparaíso",
                    "Biobío",
                    "Maule"
            };

            for (String nombre : regionesBase) {
                RegionModel r = new RegionModel();
                r.setNombre(nombre.trim());
                regionRepository.save(r);
            }
        }

        List<RegionModel> regiones = regionRepository.findAll();

        // ================== COMUNAS ==================
        if (comunaRepository.count() == 0) {
            for (RegionModel region : regiones) {

                String[] comunasBase = switch (region.getNombre()) {
                    case "Región Metropolitana" -> new String[] { "Santiago", "Providencia", "Ñuñoa" };
                    case "Valparaíso" -> new String[] { "Viña del Mar", "Valparaíso", "Quilpué" };
                    case "Biobío" -> new String[] { "Concepción", "Talcahuano" };
                    default -> new String[] { "Comuna Genérica 1", "Comuna Genérica 2" };
                };

                for (String c : comunasBase) {
                    ComunaModel comuna = new ComunaModel();
                    comuna.setNombre(c.trim());
                    comuna.setRegion(region);
                    comunaRepository.save(comuna);
                }
            }
        }

        List<ComunaModel> comunas = comunaRepository.findAll();

        // ================== USUARIOS ==================
        if (usuarioRepository.count() == 0) {

            for (int i = 0; i < 10; i++) {

                UsuarioModel u = new UsuarioModel();
                u.setNombre(faker.name().firstName());
                u.setApellidos(faker.name().lastName());
                u.setCorreo("user" + i + "@example.com");
                u.setTelefono(900000000L + random.nextInt(100000000));
                u.setPassword("clave" + i);
                u.setRol(rolCliente);

                UsuarioModel usuarioGuardado = usuarioRepository.save(u);

                DireccionModel d = new DireccionModel();
                d.setCalle(faker.address().streetName());
                d.setNumero(String.valueOf(faker.number().numberBetween(1, 2000)));
                d.setCodigoPostal(String.valueOf(faker.number().numberBetween(1000000, 9999999)));

                ComunaModel comunaRandom = comunas.get(random.nextInt(comunas.size()));
                d.setComuna(comunaRandom);
                d.setUsuarioModel(usuarioGuardado);

                direccionRepository.save(d);
            }
        }

        List<UsuarioModel> usuarios = usuarioRepository.findAll();

        // ================== PRODUCTOS RANDOM ==================
        for (int i = 0; i < 30; i++) {

            String nombre = faker.commerce().productName();
            if (nombre.length() > 100) {
                nombre = nombre.substring(0, 100);
            }

            if (productoRepository.existsByNombreIgnoreCase(nombre)) {
                continue;
            }

            ProductoModel p = new ProductoModel();
            p.setNombre(nombre.trim());

            double precio = faker.number().randomDouble(0, 5_000, 50_000);
            p.setPrecio(precio <= 0 ? 9_990 : precio);

            p.setTipoProducto(tiposProducto.get(random.nextInt(tiposProducto.size())));
            p.setEstado(estados.get(random.nextInt(estados.size())));
            p.setClasificacion(clasificaciones.get(random.nextInt(clasificaciones.size())));

            productoRepository.save(p);
        }

        // ================== PRODUCTOS CON SAGAS (PELÍCULAS) ==================
        TipoProductoModel tipoPelicula = tiposProducto.stream()
                .filter(t -> t.getNombre().equalsIgnoreCase("Película"))
                .findFirst()
                .orElse(null);

        EstadoModel estadoActivo = estados.stream()
                .filter(e -> e.getNombre().equalsIgnoreCase("Activo"))
                .findFirst()
                .orElse(estados.get(0));

        ClasificacionModel clasif13 = clasificaciones.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase("+13"))
                .findFirst()
                .orElse(clasificaciones.get(0));

        if (tipoPelicula != null) {

            String portadaSpiderman = "/assets/img/sagas/spidermanSaga.webp";

            crearPeliculaSiNoExiste(
                    "Spider-Man (2002)",
                    "Spiderman",
                    portadaSpiderman,
                    12990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );

            crearPeliculaSiNoExiste(
                    "Spider-Man 2 (2004)",
                    "Spiderman",
                    portadaSpiderman,
                    13990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );

            crearPeliculaSiNoExiste(
                    "Spider-Man 3 (2007)",
                    "Spiderman",
                    portadaSpiderman,
                    14990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );

            String portadaLotr = "/assets/img/sagas/lotrSaga.webp";

            crearPeliculaSiNoExiste(
                    "El Señor de los Anillos: La Comunidad del Anillo",
                    "El Señor de los Anillos",
                    portadaLotr,
                    15990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );

            crearPeliculaSiNoExiste(
                    "El Señor de los Anillos: Las Dos Torres",
                    "El Señor de los Anillos",
                    portadaLotr,
                    15990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );

            crearPeliculaSiNoExiste(
                    "El Señor de los Anillos: El Retorno del Rey",
                    "El Señor de los Anillos",
                    portadaLotr,
                    16990.0,
                    tipoPelicula,
                    clasif13,
                    estadoActivo
            );
        }

        // Recarga productos luego de crear las sagas
        List<ProductoModel> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            return;
        }

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

        // ================== ASIGNAR PLATAFORMAS A PRODUCTOS (TP Plataformas) ==================
        if (!plataformas.isEmpty()) {
            for (ProductoModel producto : productos) {

                int cantidadPlataformas = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadPlataformas; i++) {
                    PlataformaModel plataformaRandom = plataformas.get(random.nextInt(plataformas.size()));

                    if (!plataformasRepository
                            .existsByProducto_IdAndPlataforma_Id(producto.getId(), plataformaRandom.getId())) {

                        PlataformasModel rel = new PlataformasModel();
                        rel.setProducto(producto);
                        rel.setPlataforma(plataformaRandom);
                        plataformasRepository.save(rel);
                    }
                }
            }
        }

        // ================== ASIGNAR DESARROLLADORES A PRODUCTOS (TP) ==================
        if (!desarrolladores.isEmpty()) {
            for (ProductoModel producto : productos) {

                int cantidadDevs = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadDevs; i++) {
                    DesarrolladorModel devRandom = desarrolladores.get(random.nextInt(desarrolladores.size()));

                    if (!desarrolladoresRepository
                            .existsByProducto_IdAndDesarrollador_Id(producto.getId(), devRandom.getId())) {

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

        // ================== ASIGNAR EMPRESAS A PRODUCTOS (TP Empresas) ==================
        if (!empresas.isEmpty()) {
            for (ProductoModel producto : productos) {

                int cantidadEmpresas = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadEmpresas; i++) {
                    EmpresaModel empresaRandom = empresas.get(random.nextInt(empresas.size()));

                    if (!empresasRepository
                            .existsByProducto_IdAndEmpresa_Id(producto.getId(), empresaRandom.getId())) {

                        EmpresasModel rel = new EmpresasModel();
                        rel.setProducto(producto);
                        rel.setEmpresa(empresaRandom);
                        empresasRepository.save(rel);
                    }
                }
            }
        }

        // ================== ASIGNAR TIPOS A EMPRESAS (TP TiposEmpresa) ==================
        if (!empresas.isEmpty() && !tiposEmpresa.isEmpty()) {
            for (EmpresaModel empresa : empresas) {

                int cantidadTipos = 1 + random.nextInt(3);

                for (int i = 0; i < cantidadTipos; i++) {
                    TipoEmpresaModel tipoRandom = tiposEmpresa.get(random.nextInt(tiposEmpresa.size()));

                    if (!tiposEmpresaRepository
                            .existsByEmpresa_IdAndTipoEmpresa_Id(empresa.getId(), tipoRandom.getId())) {

                        TiposEmpresaModel link = new TiposEmpresaModel();
                        link.setEmpresa(empresa);
                        link.setTipoEmpresa(tipoRandom);
                        tiposEmpresaRepository.save(link);
                    }
                }
            }
        }

        // ================== VENTAS Y DETALLES ==================
        if (!usuarios.isEmpty() && !metodosPago.isEmpty() && !metodosEnvio.isEmpty()) {

            for (int i = 0; i < 20; i++) {

                VentaModel venta = new VentaModel();
                venta.setFechaCompra(LocalDate.now().minusDays(random.nextInt(30)));
                venta.setHoraCompra(LocalTime.of(random.nextInt(23), random.nextInt(60)));
                venta.setUsuarioModel(usuarios.get(random.nextInt(usuarios.size())));
                venta.setMetodoPagoModel(metodosPago.get(random.nextInt(metodosPago.size())));
                venta.setMetodoEnvioModel(metodosEnvio.get(random.nextInt(metodosEnvio.size())));
                venta.setEstado(estados.get(random.nextInt(estados.size())));

                VentaModel ventaGuardada = ventaRepository.save(venta);

                int cantidadDetalles = 1 + random.nextInt(4);
                for (int j = 0; j < cantidadDetalles; j++) {
                    ProductoModel productoRandom = productos.get(random.nextInt(productos.size()));

                    DetalleVentaModel det = new DetalleVentaModel();
                    det.setVenta(ventaGuardada);
                    det.setProducto(productoRandom);
                    det.setCantidad(1 + random.nextInt(3));
                    det.setPrecioUnitario(productoRandom.getPrecio().floatValue());

                    detalleVentaRepository.save(det);
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
                "[DataLoader] Seed dev completo: tipos de producto, estados, clasificaciones, géneros, "
                        + "desarrolladores, tipos de desarrollador, empresas, tipos de empresa, plataformas, "
                        + "usuarios, métodos de pago, métodos de envío, regiones, comunas, direcciones, "
                        + "ventas, detalles de venta, relaciones TP, productos e imágenes.");
    }

    /**
     * Crea una película de saga si no existe un producto con ese nombre.
     */
    private void crearPeliculaSiNoExiste(
            String nombre,
            String saga,
            String portadaSaga,
            Double precio,
            TipoProductoModel tipoPelicula,
            ClasificacionModel clasificacion,
            EstadoModel estado
    ) {
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            return;
        }

        ProductoModel p = new ProductoModel();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setTipoProducto(tipoPelicula);
        p.setClasificacion(clasificacion);
        p.setEstado(estado);

        // Campos nuevos para carrusel de sagas
        p.setSaga(saga);
        p.setPortadaSaga(portadaSaga);

        productoRepository.save(p);
    }
}