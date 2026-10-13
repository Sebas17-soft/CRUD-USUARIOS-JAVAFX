package vista;

import controlador.UsuarioController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import modelo.Usuario;

import java.util.Optional;

/**
 * Vista principal de la aplicación construida con componentes JavaFX.
 * Implementa una interfaz gráfica moderna, elegante, modular y responsive.
 */
public class MainView extends BorderPane {

    // Controlador de la aplicación
    private final UsuarioController controller;

    // Componentes del formulario
    private TextField txtId;
    private TextField txtNombre;
    private TextField txtCorreo;
    private PasswordField txtPassword;
    private TextField txtPasswordVisible;
    private CheckBox chkVerPassword;

    // Botones de acción
    private Button btnGuardar;
    private Button btnModificar;
    private Button btnEliminar;
    private Button btnLimpiar;

    // Tabla de usuarios
    private TableView<Usuario> tablaUsuarios;
    private TextField txtBuscar;
    private Label lblContadorUsuarios;
    private Label lblEstadoModo;

    // Barra de estado y mensajes
    private Label lblMensajeEstado;
    private HBox barraEstado;

    /**
     * Constructor de la vista principal.
     *
     * @param controller Instancia del controlador que gestiona los datos en memoria.
     */
    public MainView(UsuarioController controller) {
        this.controller = controller;
        this.getStyleClass().add("root-pane");

        // Construcción de la interfaz
        setTop(crearEncabezado());
        setCenter(crearContenidoPrincipal());
        setBottom(crearBarraEstado());

        // Configuración de eventos y enlaces
        configurarEventos();
        actualizarEstadoModo(false);
        mostrarMensaje("Listo. Sistema cargado en memoria correctamente.", "info");
    }

    // ==========================================
    // CONSTRUCCIÓN DE COMPONENTES VISUALES
    // ==========================================

    /**
     * Construye el encabezado superior con título corporativo y badge de estado.
     */
    private VBox crearEncabezado() {
        VBox encabezado = new VBox();
        encabezado.getStyleClass().add("header-panel");
        encabezado.setPadding(new Insets(18, 25, 18, 25));
        encabezado.setSpacing(6);

        HBox filaTitulo = new HBox();
        filaTitulo.setAlignment(Pos.CENTER_LEFT);
        filaTitulo.setSpacing(15);

        Label lblTitulo = new Label("SISTEMA DE GESTIÓN DE USUARIOS");
        lblTitulo.getStyleClass().add("header-title");

        Region separador = new Region();
        HBox.setHgrow(separador, Priority.ALWAYS);

        lblContadorUsuarios = new Label("Usuarios: 0");
        lblContadorUsuarios.getStyleClass().add("badge-counter");

        filaTitulo.getChildren().addAll(lblTitulo, separador, lblContadorUsuarios);

        Label lblSubtitulo = new Label("Operaciones CRUD en memoria (ArrayList) con JavaFX & CSS");
        lblSubtitulo.getStyleClass().add("header-subtitle");

        encabezado.getChildren().addAll(filaTitulo, lblSubtitulo);
        return encabezado;
    }

    /**
     * Crea la sección central dividida en el panel de formulario (izq) y la tabla (der).
     */
    private HBox crearContenidoPrincipal() {
        HBox contenedor = new HBox(20);
        contenedor.setPadding(new Insets(20));
        HBox.setHgrow(contenedor, Priority.ALWAYS);

        VBox panelFormulario = crearPanelFormulario();
        VBox panelTabla = crearPanelTabla();

        // Proporciones responsive
        panelFormulario.setPrefWidth(380);
        panelFormulario.setMinWidth(340);
        HBox.setHgrow(panelTabla, Priority.ALWAYS);

        contenedor.getChildren().addAll(panelFormulario, panelTabla);
        return contenedor;
    }

    /**
     * Crea el panel de la tarjeta del formulario de registro y edición.
     */
    private VBox crearPanelFormulario() {
        VBox card = new VBox(16);
        card.getStyleClass().add("card-panel");
        card.setPadding(new Insets(22));

        // Título del formulario
        HBox cabeceraCard = new HBox();
        cabeceraCard.setAlignment(Pos.CENTER_LEFT);

        Label lblTituloForm = new Label("Formulario de Usuario");
        lblTituloForm.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lblEstadoModo = new Label("Nuevo");
        lblEstadoModo.getStyleClass().add("badge-modo-nuevo");

        cabeceraCard.getChildren().addAll(lblTituloForm, spacer, lblEstadoModo);

        // Campos del formulario
        VBox campoId = crearCampo("ID de Usuario:", txtId = new TextField());
        txtId.setEditable(false);
        txtId.setPromptText("Autogenerado");
        txtId.getStyleClass().add("input-id");

        VBox campoNombre = crearCampo("Nombre Completo *", txtNombre = new TextField());
        txtNombre.setPromptText("Ej. Carlos Mendoza");

        VBox campoCorreo = crearCampo("Correo Electrónico *", txtCorreo = new TextField());
        txtCorreo.setPromptText("ejemplo@correo.com");

        // Campo contraseña con toggle de visibilidad
        Label lblPass = new Label("Contraseña (mín. 6 caracteres) *");
        lblPass.getStyleClass().add("form-label");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("••••••••");

        txtPasswordVisible = new TextField();
        txtPasswordVisible.setPromptText("Ingresa tu contraseña");
        txtPasswordVisible.setManaged(false);
        txtPasswordVisible.setVisible(false);

        // Sincronizar contraseña visible y oculta
        txtPassword.textProperty().bindBidirectional(txtPasswordVisible.textProperty());

        chkVerPassword = new CheckBox("Mostrar contraseña");
        chkVerPassword.getStyleClass().add("chk-mostrar-pass");
        chkVerPassword.setOnAction(e -> {
            boolean visible = chkVerPassword.isSelected();
            txtPasswordVisible.setVisible(visible);
            txtPasswordVisible.setManaged(visible);
            txtPassword.setVisible(!visible);
            txtPassword.setManaged(!visible);
        });

        StackPane stackPass = new StackPane(txtPassword, txtPasswordVisible);
        VBox campoPassword = new VBox(6, lblPass, stackPass, chkVerPassword);

        // Panel de botones
        GridPane gridBotones = new GridPane();
        gridBotones.setHgap(10);
        gridBotones.setVgap(12);

        btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().addAll("btn-accion", "btn-guardar");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);

        btnModificar = new Button("Modificar");
        btnModificar.getStyleClass().addAll("btn-accion", "btn-modificar");
        btnModificar.setMaxWidth(Double.MAX_VALUE);

        btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().addAll("btn-accion", "btn-eliminar");
        btnEliminar.setMaxWidth(Double.MAX_VALUE);

        btnLimpiar = new Button("Limpiar");
        btnLimpiar.getStyleClass().addAll("btn-accion", "btn-limpiar");
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        gridBotones.getColumnConstraints().addAll(col1, col2);

        gridBotones.add(btnGuardar, 0, 0);
        gridBotones.add(btnModificar, 1, 0);
        gridBotones.add(btnEliminar, 0, 1);
        gridBotones.add(btnLimpiar, 1, 1);

        card.getChildren().addAll(
                cabeceraCard,
                new Separator(),
                campoId,
                campoNombre,
                campoCorreo,
                campoPassword,
                new Region(),
                gridBotones
        );

        return card;
    }

    /**
     * Auxiliar para envolver una etiqueta y su campo de texto en un VBox ordenado.
     */
    private VBox crearCampo(String etiquetaTexto, TextField input) {
        Label etiqueta = new Label(etiquetaTexto);
        etiqueta.getStyleClass().add("form-label");
        input.getStyleClass().add("input-text");
        return new VBox(6, etiqueta, input);
    }

    /**
     * Crea el panel con la tabla de usuarios y la barra de búsqueda en tiempo real.
     */
    private VBox crearPanelTabla() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card-panel");
        card.setPadding(new Insets(22));
        VBox.setVgrow(card, Priority.ALWAYS);

        // Título de la tabla y buscador
        HBox barraSuperiorTabla = new HBox(12);
        barraSuperiorTabla.setAlignment(Pos.CENTER_LEFT);

        Label lblTituloTabla = new Label("Usuarios Registrados");
        lblTituloTabla.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por ID, nombre o correo...");
        txtBuscar.getStyleClass().add("input-buscar");
        txtBuscar.setPrefWidth(260);

        barraSuperiorTabla.getChildren().addAll(lblTituloTabla, spacer, txtBuscar);

        // Configuración de la TableView
        tablaUsuarios = new TableView<>();
        tablaUsuarios.getStyleClass().add("tabla-usuarios");
        tablaUsuarios.setPlaceholder(new Label("No hay usuarios registrados en memoria."));
        VBox.setVgrow(tablaUsuarios, Priority.ALWAYS);

        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(65);
        colId.setStyle("-fx-alignment: CENTER;");

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre Completo");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(190);

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo Electrónico");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCorreo.setPrefWidth(220);

        TableColumn<Usuario, String> colPassword = new TableColumn<>("Contraseña");
        // Ocultar parcialmente contraseña en la tabla por seguridad pero mostrando su contenido
        colPassword.setCellValueFactory(cellData -> {
            String pass = cellData.getValue().getContrasena();
            if (pass == null) return new SimpleStringProperty("");
            return new SimpleStringProperty("•".repeat(Math.min(pass.length(), 10)));
        });
        colPassword.setPrefWidth(120);
        colPassword.setStyle("-fx-alignment: CENTER;");

        tablaUsuarios.getColumns().addAll(colId, colNombre, colCorreo, colPassword);
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Integrar FilteredList para búsqueda en tiempo real
        FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(controller.getUsuariosObservables(), p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            usuariosFiltrados.setPredicate(usuario -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String filtro = newValue.toLowerCase().trim();
                return String.valueOf(usuario.getId()).contains(filtro)
                        || (usuario.getNombre() != null && usuario.getNombre().toLowerCase().contains(filtro))
                        || (usuario.getCorreo() != null && usuario.getCorreo().toLowerCase().contains(filtro));
            });
        });

        SortedList<Usuario> usuariosOrdenados = new SortedList<>(usuariosFiltrados);
        usuariosOrdenados.comparatorProperty().bind(tablaUsuarios.comparatorProperty());
        tablaUsuarios.setItems(usuariosOrdenados);

        card.getChildren().addAll(barraSuperiorTabla, new Separator(), tablaUsuarios);
        return card;
    }

    /**
     * Construye la barra de estado inferior para retroalimentación visual al usuario.
     */
    private HBox crearBarraEstado() {
        barraEstado = new HBox(10);
        barraEstado.getStyleClass().add("status-bar");
        barraEstado.setPadding(new Insets(10, 20, 10, 20));
        barraEstado.setAlignment(Pos.CENTER_LEFT);

        lblMensajeEstado = new Label("Listo.");
        lblMensajeEstado.getStyleClass().add("status-label");

        barraEstado.getChildren().add(lblMensajeEstado);
        return barraEstado;
    }

    // ==========================================
    // CONTROLADORES DE EVENTOS
    // ==========================================

    /**
     * Asigna las acciones a los botones, tabla y elementos interactivos.
     */
    private void configurarEventos() {
        // Evento botón GUARDAR
        btnGuardar.setOnAction(e -> accionGuardar());

        // Evento botón MODIFICAR
        btnModificar.setOnAction(e -> accionModificar());

        // Evento botón ELIMINAR
        btnEliminar.setOnAction(e -> accionEliminar());

        // Evento botón LIMPIAR
        btnLimpiar.setOnAction(e -> accionLimpiar());

        // Evento selección en la tabla: carga automáticamente en el formulario
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nuevoSel) -> {
            if (nuevoSel != null) {
                controller.cargarDatosEnFormulario(nuevoSel, txtId, txtNombre, txtCorreo, txtPassword);
                actualizarEstadoModo(true);
                mostrarMensaje("Usuario #" + nuevoSel.getId() + " seleccionado para edición.", "info");
            } else {
                actualizarEstadoModo(false);
            }
        });

        // Actualizar contador cada vez que cambie la lista
        controller.getUsuariosObservables().addListener((javafx.collections.ListChangeListener<Usuario>) c -> {
            actualizarContador();
        });
        actualizarContador();
    }

    /**
     * Lógica para registrar un nuevo usuario en memoria.
     */
    private void accionGuardar() {
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String password = txtPassword.getText();

        try {
            Usuario nuevo = controller.agregarUsuario(nombre, correo, password);
            controller.limpiarCampos(txtId, txtNombre, txtCorreo, txtPassword, tablaUsuarios);
            actualizarEstadoModo(false);
            mostrarMensaje("Usuario '" + nuevo.getNombre() + "' registrado exitosamente en memoria.", "exito");
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), "error");
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Registro", ex.getMessage());
        }
    }

    /**
     * Lógica para actualizar un usuario seleccionado en memoria.
     */
    private void accionModificar() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            String err = "Debe seleccionar un usuario de la tabla para poder modificarlo.";
            mostrarMensaje(err, "error");
            mostrarAlerta(Alert.AlertType.WARNING, "Acción requerida", err);
            return;
        }

        String nuevoNombre = txtNombre.getText();
        String nuevoCorreo = txtCorreo.getText();
        String nuevaPass = txtPassword.getText();

        try {
            boolean actualizado = controller.actualizarUsuario(seleccionado.getId(), nuevoNombre, nuevoCorreo, nuevaPass);
            if (actualizado) {
                tablaUsuarios.refresh();
                controller.limpiarCampos(txtId, txtNombre, txtCorreo, txtPassword, tablaUsuarios);
                actualizarEstadoModo(false);
                mostrarMensaje("Usuario #" + seleccionado.getId() + " actualizado correctamente.", "exito");
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), "error");
            mostrarAlerta(Alert.AlertType.WARNING, "Error al modificar", ex.getMessage());
        }
    }

    /**
     * Lógica para eliminar un usuario en memoria con confirmación.
     */
    private void accionEliminar() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            String err = "Debe seleccionar un usuario de la tabla para poder eliminarlo.";
            mostrarMensaje(err, "error");
            mostrarAlerta(Alert.AlertType.WARNING, "Acción requerida", err);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar al usuario?");
        confirmacion.setContentText("Usuario: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")\n"
                + "Esta acción lo eliminará de la memoria del programa.");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            boolean eliminado = controller.eliminarUsuario(seleccionado.getId());
            if (eliminado) {
                controller.limpiarCampos(txtId, txtNombre, txtCorreo, txtPassword, tablaUsuarios);
                actualizarEstadoModo(false);
                mostrarMensaje("Usuario eliminado exitosamente de la memoria.", "exito");
            } else {
                mostrarMensaje("No se pudo eliminar el usuario de la memoria.", "error");
            }
        }
    }

    /**
     * Limpia los campos del formulario y deselecciona la tabla.
     */
    private void accionLimpiar() {
        controller.limpiarCampos(txtId, txtNombre, txtCorreo, txtPassword, tablaUsuarios);
        actualizarEstadoModo(false);
        mostrarMensaje("Formulario limpio. Modo nuevo registro activo.", "info");
    }

    /**
     * Actualiza el badge visual de modo (Nuevo vs Edición) y habilita/deshabilita controles.
     */
    private void actualizarEstadoModo(boolean enEdicion) {
        if (enEdicion) {
            lblEstadoModo.setText("Edición");
            lblEstadoModo.getStyleClass().clear();
            lblEstadoModo.getStyleClass().add("badge-modo-edicion");
            btnGuardar.setDisable(true);
            btnModificar.setDisable(false);
            btnEliminar.setDisable(false);
        } else {
            lblEstadoModo.setText("Nuevo");
            lblEstadoModo.getStyleClass().clear();
            lblEstadoModo.getStyleClass().add("badge-modo-nuevo");
            btnGuardar.setDisable(false);
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
        }
    }

    /**
     * Actualiza el contador de usuarios registrados en el encabezado.
     */
    private void actualizarContador() {
        int total = controller.getUsuariosObservables().size();
        lblContadorUsuarios.setText("Usuarios en memoria: " + total);
    }

    /**
     * Muestra un mensaje en la barra de estado con colores dinámicos.
     */
    private void mostrarMensaje(String mensaje, String tipo) {
        lblMensajeEstado.setText(mensaje);
        barraEstado.getStyleClass().removeAll("status-info", "status-exito", "status-error");
        switch (tipo) {
            case "exito":
                barraEstado.getStyleClass().add("status-exito");
                break;
            case "error":
                barraEstado.getStyleClass().add("status-error");
                break;
            default:
                barraEstado.getStyleClass().add("status-info");
                break;
        }
    }

    /**
     * Muestra una ventana de diálogo de alerta estándar.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
