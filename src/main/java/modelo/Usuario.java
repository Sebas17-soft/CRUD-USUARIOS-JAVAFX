package modelo;

import java.util.Objects;

/**
 * Clase que representa a la entidad Usuario dentro del sistema.
 * Contiene los atributos básicos del usuario y los métodos de acceso.
 * Se almacena únicamente en memoria durante la ejecución de la aplicación.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    /**
     * Constructor por defecto.
     */
    public Usuario() {
    }

    /**
     * Constructor con todos los atributos.
     *
     * @param id          Identificador único del usuario.
     * @param nombre      Nombre completo del usuario.
     * @param correo      Correo electrónico válido y único.
     * @param contrasena  Contraseña de acceso.
     */
    public Usuario(int id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Alias con 'ñ' para compatibilidad si se requiere invocar getContraseña().
     */
    public String getContraseña() {
        return this.contrasena;
    }

    /**
     * Alias con 'ñ' para compatibilidad si se requiere invocar setContraseña().
     */
    public void setContraseña(String contraseña) {
        this.contrasena = contraseña;
    }

    // ==========================================
    // MÉTODOS SOBRESCRITOS
    // ==========================================

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasena='******'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
