/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe;

import cafe.control.MainController;
import cafe.control.PageRenderer;
import cafe.view.ContentPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
/**
 *
 * @author chibayuuki
 */
public class Carbonara extends Application {
    public static final int DEFAULT_WINDOW_WIDTH = 1080;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;
    public static final String APPLICATION_NAME = "カルボナーラ Webページエディター";
    public static final String APPLICATION_SCREEN_NAME = "Carbonara";
    
    private static Scene scene;
    private static Stage stage;
    private static ContentPane root;
    private static MainController controller;
    private static PageRenderer renderer;
    private static Application application;
    private static Image icon;
    private static String icon_image_path = "resources/images/CarbonaraLogo.png";

    private static void processForMacOSX() throws Exception {
        icon = ImageIO.read(new File(Carbonara.icon_image_path));
        com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
        app.setDockIconImage(icon);
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", Carbonara.APPLICATION_SCREEN_NAME);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Carbonara.processForMacOSX();
        }catch(Exception e){
            System.err.println("OS is not MacOSX");
        }
        
        Carbonara.controller = new MainController();
        Carbonara.renderer = new PageRenderer();
        
        Carbonara.stage = primaryStage;
        Carbonara.root = new ContentPane(Carbonara.controller);
        Carbonara.root.setWindow(Carbonara.stage);
        
        Carbonara.renderer.setView(root);
        Carbonara.controller.setRenderer(renderer);
        Carbonara.controller.setContentPane(root);
        
        Carbonara.scene = new Scene(Carbonara.root, Color.color(1.0,0.98,0.85));
        Carbonara.stage.setScene(Carbonara.scene);
        Carbonara.stage.getScene().getWindow().focusedProperty().addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean old, Boolean value) {
                if(value){
                    Carbonara.stage.setOpacity(1.0f);
                }
                else{
                    Carbonara.stage.setOpacity(0.8f);
                }
            }
        });
        Carbonara.stage.setOpacity(0.8);
        
        Carbonara.stage.setWidth(DEFAULT_WINDOW_WIDTH);
        Carbonara.stage.setHeight(DEFAULT_WINDOW_HEIGHT);
        
        Carbonara.stage.show();
        Carbonara.root.updateWindowSize();
        
        Carbonara.stage.getIcons().clear();
        Carbonara.stage.getIcons().add(new javafx.scene.image.Image(Carbonara.icon_image_path));
        Carbonara.stage.setTitle(Carbonara.APPLICATION_NAME);
        
        Carbonara.controller.start();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    public static MainController AppController(){
        return Carbonara.controller;
    }
    public static PageRenderer Renderer(){
        return Carbonara.renderer;
    }
}
