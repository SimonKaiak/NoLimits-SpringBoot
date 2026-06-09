package com.example.NoLimits.config;

import com.example.NoLimits.Multimedia.config.RolesSeeder;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.repository.usuario.RolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RolesSeeder")
class RolesSeederTest {

    @Nested
    @DisplayName("describe: seedRoles")
    class SeedRoles {

        @Test
        @DisplayName("it: debería crear ROLE_USER y ROLE_ADMIN cuando no existen")
        void deberiaCrearRolesCuandoNoExisten() throws Exception {
            RolesSeeder seeder = new RolesSeeder();
            RolRepository rolRepository = mock(RolRepository.class);

            when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                    .thenReturn(Optional.empty());
            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.empty());

            CommandLineRunner runner = ReflectionTestUtils.invokeMethod(
                    seeder,
                    "seedRoles",
                    rolRepository
            );

            runner.run();

            ArgumentCaptor<RolModel> rolCaptor = ArgumentCaptor.forClass(RolModel.class);
            verify(rolRepository, times(2)).save(rolCaptor.capture());

            RolModel user = rolCaptor.getAllValues().get(0);
            RolModel admin = rolCaptor.getAllValues().get(1);

            assertEquals("ROLE_USER", user.getNombre());
            assertEquals("Rol por defecto para usuarios", user.getDescripcion());
            assertTrue(user.esActivo());

            assertEquals("ROLE_ADMIN", admin.getNombre());
            assertEquals("Rol administrador del sistema", admin.getDescripcion());
            assertTrue(admin.esActivo());
        }

        @Test
        @DisplayName("it: debería crear solo ROLE_ADMIN cuando ROLE_USER ya existe")
        void deberiaCrearSoloRoleAdminCuandoRoleUserYaExiste() throws Exception {
            RolesSeeder seeder = new RolesSeeder();
            RolRepository rolRepository = mock(RolRepository.class);

            when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                    .thenReturn(Optional.of(new RolModel()));
            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.empty());

            CommandLineRunner runner = ReflectionTestUtils.invokeMethod(
                    seeder,
                    "seedRoles",
                    rolRepository
            );

            runner.run();

            ArgumentCaptor<RolModel> rolCaptor = ArgumentCaptor.forClass(RolModel.class);
            verify(rolRepository, times(1)).save(rolCaptor.capture());

            RolModel admin = rolCaptor.getValue();

            assertEquals("ROLE_ADMIN", admin.getNombre());
            assertEquals("Rol administrador del sistema", admin.getDescripcion());
            assertTrue(admin.esActivo());
        }

        @Test
        @DisplayName("it: debería crear solo ROLE_USER cuando ROLE_ADMIN ya existe")
        void deberiaCrearSoloRoleUserCuandoRoleAdminYaExiste() throws Exception {
            RolesSeeder seeder = new RolesSeeder();
            RolRepository rolRepository = mock(RolRepository.class);

            when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                    .thenReturn(Optional.empty());
            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.of(new RolModel()));

            CommandLineRunner runner = ReflectionTestUtils.invokeMethod(
                    seeder,
                    "seedRoles",
                    rolRepository
            );

            runner.run();

            ArgumentCaptor<RolModel> rolCaptor = ArgumentCaptor.forClass(RolModel.class);
            verify(rolRepository, times(1)).save(rolCaptor.capture());

            RolModel user = rolCaptor.getValue();

            assertEquals("ROLE_USER", user.getNombre());
            assertEquals("Rol por defecto para usuarios", user.getDescripcion());
            assertTrue(user.esActivo());
        }

        @Test
        @DisplayName("it: no debería crear roles cuando ambos ya existen")
        void noDeberiaCrearRolesCuandoAmbosYaExisten() throws Exception {
            RolesSeeder seeder = new RolesSeeder();
            RolRepository rolRepository = mock(RolRepository.class);

            when(rolRepository.findByNombreIgnoreCase("ROLE_USER"))
                    .thenReturn(Optional.of(new RolModel()));
            when(rolRepository.findByNombreIgnoreCase("ROLE_ADMIN"))
                    .thenReturn(Optional.of(new RolModel()));

            CommandLineRunner runner = ReflectionTestUtils.invokeMethod(
                    seeder,
                    "seedRoles",
                    rolRepository
            );

            runner.run();

            verify(rolRepository, never()).save(any(RolModel.class));
        }
    }
}