package lifetracker;

import lifetracker.UI.UI;
import lifetracker.UI.UIController;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicImpl;
import lifetracker.parser.Parser;
import lifetracker.parser.ParserImpl;
import lifetracker.storage.Storage;
import lifetracker.storage.ThreadedFileStorage;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sun.j3d.utils.scenegraph.io.retained.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LifeTracker extends Application {

    private static final String LOG_FOLDER = "logs/";
    private static final String LOG_FILE = "lifetracker.log";

    public static void main(String args[]) throws Exception {
        launch(args);
    }

    private static void setLogger() throws IOException {

        File logDir = new File(LOG_FOLDER);

        if (!logDir.exists()) {
            logDir.mkdir();
        }

        LogManager.getLogManager().reset();

        Logger globalLogger = Logger.getGlobal();
        globalLogger.addHandler(new FileHandler(LOG_FOLDER + LOG_FILE));

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lifetracker/UI/UIDesign.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/lifetracker/UI/application.css").toExternalForm());
        primaryStage.setTitle("Life Tracker");
        primaryStage.setScene(scene);
        
        
        try (Storage fileStorage = new ThreadedFileStorage()) {

            setLogger();
            Parser commandParser = new ParserImpl();
            Logic programLogic = new LogicImpl(commandParser, fileStorage);
            new UIController(programLogic);
            
        }
        
        
        primaryStage.show();
    }
}
