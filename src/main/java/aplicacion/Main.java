package aplicacion;

import controlador.UsuarioController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import vista.MainView;

import java.net.URL;

/**
 * Clase principal que inicializa el ciclo de vida de la aplicación JavaFX.
 * Configura la ventana principal, el controlador, la vista y aplica las hojas de estilo CSS.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Instanciar el controlador de la lógica en memoria
        UsuarioController controller = new UsuarioController();

        // 2. Instanciar la interfaz gráfica principal vinculada al controlador
        MainView root = new MainView(controller);

        // 3. Crear la escena con dimensiones óptimas para escritorio
        Scene scene = new Scene(root, 1080, 700);

        // 4. Cargar la hoja de estilos CSS personalizada
        URL cssUrl = getClass().getResource("/estilos.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Advertencia: No se encontró el archivo /estilos.css en el classpath.");
        }

        // 5. Configurar propiedades de la ventana (Stage)
        primaryStage.setTitle("Gestor Profesional de Usuarios - JavaFX (CRUD en Memoria)");
        primaryStage.setMinWidth(920);
        primaryStage.setMinHeight(620);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        // 6. Mostrar la ventana
        primaryStage.show();
    }

    /**
     * Punto de entrada principal de la aplicación Java.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
