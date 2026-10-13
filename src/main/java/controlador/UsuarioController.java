package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Controlador de la lógica de negocio para la gestión de usuarios.
 * Administra el almacenamiento en memoria mediante un ArrayList<Usuario>
 * y expone una lista observable sincronizada para la interfaz gráfica JavaFX.
 */
public class UsuarioController {

    /**
     * Estructura principal en memoria según los requerimientos.
     */
    private final ArrayList<Usuario> listaUsuarios;

    /**
     * Lista observable para vincular reactivamente con la TableView de JavaFX.
     */
    private final ObservableList<Usuario> usuariosObservables;

    /**
     * Generador secuencial de IDs automáticos.
     */
    private int secuenciaId;

    /**
     * Expresión regular estándar para validación de formato de correo electrónico.
     */
    private static final Pattern PATRON_CORREO = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    /**
     * Constructor del controlador. Inicializa las estructuras en memoria y carga datos de demostración.
     */
    public UsuarioController() {
        this.listaUsuarios = new ArrayList<>();
        this.usuariosObservables = FXCollections.observableArrayList();
        this.secuenciaId = 1;

        // Cargar algunos datos iniciales de demostración para verificar de inmediato
        precargarUsuariosIniciales();
    }

    /**
     * Precarga usuarios iniciales en memoria para que la aplicación muestre registros de inmediato.
     */
    private void precargarUsuariosIniciales() {
        agregarUsuario("Carlos Mendoza", "carlos.mendoza@empresa.com", "Admin#2026");
        agregarUsuario("Laura Gómez", "laura.gomez@empresa.com", "Segura*123");
        agregarUsuario("Andrés Morales", "andres.m@empresa.com", "Pass789!");
    }

    // ==========================================
    // OPERACIONES CRUD EN MEMORIA
    // ==========================================

    /**
     * Agrega un nuevo usuario al ArrayList en memoria y sincroniza la vista.
     *
     * @param nombre     Nombre completo del usuario.
     * @param correo     Correo electrónico único.
     * @param contrasena Contraseña del usuario.
     * @return El objeto Usuario creado o null si falla la validación.
     */
    public Usuario agregarUsuario(String nombre, String correo, String contrasena) {
        String error = validarDatos(nombre, correo, contrasena, null);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Usuario nuevoUsuario = new Usuario(secuenciaId++, nombre.trim(), correo.trim(), contrasena.trim());
        listaUsuarios.add(nuevoUsuario);
        sincronizarObservableList();
        return nuevoUsuario;
    }

    /**
     * Obtiene una copia o referencia directa del ArrayList de usuarios en memoria.
     *
     * @return ArrayList con todos los usuarios registrados.
     */
    public ArrayList<Usuario> obtenerUsuarios() {
        return new ArrayList<>(listaUsuarios);
    }

    /**
     * Getter directo de la lista en memoria (requerido por especificación).
     */
    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * Retorna la lista observable vinculada a los controles JavaFX.
     *
     * @return ObservableList de usuarios.
     */
    public ObservableList<Usuario> getUsuariosObservables() {
        return usuariosObservables;
    }

    /**
     * Busca un usuario en el ArrayList según su ID único.
     *
     * @param id Identificador a buscar.
     * @return El usuario encontrado, o null si no existe.
     */
    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario u : listaUsuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un usuario existente en el ArrayList.
     *
     * @param id          ID del usuario a modificar.
     * @param nuevoNombre Nuevo nombre.
     * @param nuevoCorreo Nuevo correo.
     * @param nuevaPass   Nueva contraseña.
     * @return true si se actualizó con éxito; false si no se encontró.
     */
    public boolean actualizarUsuario(int id, String nuevoNombre, String nuevoCorreo, String nuevaPass) {
        String error = validarDatos(nuevoNombre, nuevoCorreo, nuevaPass, id);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario != null) {
            usuario.setNombre(nuevoNombre.trim());
            usuario.setCorreo(nuevoCorreo.trim());
            usuario.setContrasena(nuevaPass.trim());
            sincronizarObservableList();
            return true;
        }
        return false;
    }

    /**
     * Elimina un usuario del ArrayList en memoria a partir de su ID.
     *
     * @param id Identificador del usuario a eliminar.
     * @return true si se eliminó; false si no existía.
     */
    public boolean eliminarUsuario(int id) {
        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario != null) {
            boolean eliminado = listaUsuarios.remove(usuario);
            if (eliminado) {
                sincronizarObservableList();
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // REGLAS DE NEGOCIO Y VALIDACIONES
    // ==========================================

    /**
     * Valida las reglas de negocio para registrar o modificar un usuario.
     *
     * @param nombre     Nombre del usuario.
     * @param correo     Correo del usuario.
     * @param contrasena Contraseña del usuario.
     * @param idActual   ID actual si es una modificación (para ignorar su propio correo), o null si es nuevo.
     * @return Mensaje de error descriptivo o null si todos los campos son válidos.
     */
    public String validarDatos(String nombre, String correo, String contrasena, Integer idActual) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El campo 'Nombre' es obligatorio y no puede estar vacío.";
        }
        if (nombre.trim().length() < 3) {
            return "El nombre debe tener al menos 3 caracteres.";
        }

        if (correo == null || correo.trim().isEmpty()) {
            return "El campo 'Correo' es obligatorio.";
        }
        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            return "El formato del correo electrónico no es válido (ej: usuario@dominio.com).";
        }
        if (existeCorreo(correo.trim(), idActual)) {
            return "El correo '" + correo.trim() + "' ya se encuentra registrado por otro usuario.";
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            return "El campo 'Contraseña' es obligatorio.";
        }
        if (contrasena.trim().length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }

        return null; // Todo válido
    }

    /**
     * Comprueba si un correo ya existe en el ArrayList de usuarios.
     *
     * @param correo Correo a verificar.
     * @param idExcluir ID de usuario que se debe excluir (en caso de edición), o null.
     * @return true si ya existe otro usuario con ese correo; false en caso contrario.
     */
    public boolean existeCorreo(String correo, Integer idExcluir) {
        for (Usuario u : listaUsuarios) {
            if (idExcluir != null && u.getId() == idExcluir) {
                continue; // Mismo usuario, se permite conservar su propio correo
            }
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // INTERACCIÓN CON FORMULARIO Y VISTA
    // ==========================================

    /**
     * Limpia los campos del formulario y deselecciona cualquier registro de la tabla.
     *
     * @param txtId       Campo de texto del ID.
     * @param txtNombre   Campo de texto del nombre.
     * @param txtCorreo   Campo de texto del correo.
     * @param txtPassword Campo de texto de la contraseña.
     * @param tabla       Tabla de usuarios para limpiar la selección.
     */
    public void limpiarCampos(TextField txtId, TextField txtNombre, TextField txtCorreo,
                              PasswordField txtPassword, TableView<Usuario> tabla) {
        if (txtId != null) txtId.clear();
        if (txtNombre != null) txtNombre.clear();
        if (txtCorreo != null) txtCorreo.clear();
        if (txtPassword != null) txtPassword.clear();
        if (tabla != null) tabla.getSelectionModel().clearSelection();
    }

    /**
     * Carga los datos de un usuario en los campos del formulario para su visualización o modificación.
     *
     * @param usuario     Usuario seleccionado.
     * @param txtId       Campo ID.
     * @param txtNombre   Campo Nombre.
     * @param txtCorreo   Campo Correo.
     * @param txtPassword Campo Contraseña.
     */
    public void cargarDatosEnFormulario(Usuario usuario, TextField txtId, TextField txtNombre,
                                        TextField txtCorreo, PasswordField txtPassword) {
        if (usuario != null) {
            if (txtId != null) txtId.setText(String.valueOf(usuario.getId()));
            if (txtNombre != null) txtNombre.setText(usuario.getNombre());
            if (txtCorreo != null) txtCorreo.setText(usuario.getCorreo());
            if (txtPassword != null) txtPassword.setText(usuario.getContrasena());
        }
    }

    /**
     * Sincroniza la ObservableList de JavaFX con el contenido del ArrayList en memoria.
     */
    public void sincronizarObservableList() {
        usuariosObservables.setAll(listaUsuarios);
    }
}
