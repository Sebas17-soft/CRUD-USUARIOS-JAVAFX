package controlador;

import modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar las operaciones CRUD en memoria y las reglas de negocio
 * del UsuarioController.
 */
class UsuarioControllerTest {

    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        controller = new UsuarioController();
    }

    @Test
    @DisplayName("Debe agregar un usuario válido correctamente en memoria")
    void testAgregarUsuarioExitoso() {
        int cantidadInicial = controller.obtenerUsuarios().size();
        Usuario usuario = controller.agregarUsuario("Mariana Torres", "mariana.torres@empresa.com", "ClaveSegura2026");

        assertNotNull(usuario);
        assertTrue(usuario.getId() > 0);
        assertEquals("Mariana Torres", usuario.getNombre());
        assertEquals("mariana.torres@empresa.com", usuario.getCorreo());
        assertEquals(cantidadInicial + 1, controller.obtenerUsuarios().size());
    }

    @Test
    @DisplayName("Debe rechazar un usuario con correo inválido")
    void testValidacionCorreoInvalido() {
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            controller.agregarUsuario("Pedro Picapiedra", "correo-invalido-sin-arroba", "123456");
        });

        assertTrue(excepcion.getMessage().contains("formato del correo"));
    }

    @Test
    @DisplayName("Debe rechazar contraseñas con menos de 6 caracteres")
    void testValidacionContrasenaCorta() {
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            controller.agregarUsuario("Ana López", "ana.lopez@empresa.com", "12345");
        });

        assertTrue(excepcion.getMessage().contains("al menos 6 caracteres"));
    }

    @Test
    @DisplayName("Debe impedir el registro de correos duplicados")
    void testValidacionCorreoDuplicado() {
        // carlos.mendoza@empresa.com ya viene precargado
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            controller.agregarUsuario("Otro Carlos", "carlos.mendoza@empresa.com", "MiNuevaClave123");
        });

        assertTrue(excepcion.getMessage().contains("ya se encuentra registrado"));
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente en memoria")
    void testActualizarUsuarioExitoso() {
        Usuario primero = controller.obtenerUsuarios().get(0);
        int id = primero.getId();

        boolean resultado = controller.actualizarUsuario(id, "Carlos Mendoza Editado", "carlos.nuevo@empresa.com", "NuevaPassword789");
        assertTrue(resultado);

        Usuario actualizado = controller.buscarUsuarioPorId(id);
        assertNotNull(actualizado);
        assertEquals("Carlos Mendoza Editado", actualizado.getNombre());
        assertEquals("carlos.nuevo@empresa.com", actualizado.getCorreo());
    }

    @Test
    @DisplayName("Debe eliminar un usuario existente de la memoria")
    void testEliminarUsuarioExitoso() {
        Usuario primero = controller.obtenerUsuarios().get(0);
        int id = primero.getId();
        int totalInicial = controller.obtenerUsuarios().size();

        boolean eliminado = controller.eliminarUsuario(id);
        assertTrue(eliminado);
        assertNull(controller.buscarUsuarioPorId(id));
        assertEquals(totalInicial - 1, controller.obtenerUsuarios().size());
    }
}
