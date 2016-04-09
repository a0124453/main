package lifetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lifetracker.UI.UIController;
import lifetracker.command.CommandFactoryImpl;
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

public class LifeTracker extends Application {

    private static final String LOG_FOLDER = "logs/";
    private static final String LOG_FILE = "lifetracker.log";
    private Storage fileStorage;

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
        scene.getStylesheets().add(getClass().getResource("/lifetracker/UI/application.css").toExternalForm());
        primaryStage.setTitle("Life Tracker");
        primaryStage.setScene(scene);
        fileStorage = new ThreadedFileStorage();
        setLogger();
        Parser commandParser = new ParserImpl(new CommandFactoryImpl());
        Logic programLogic = new LogicImpl(commandParser, fileStorage);
        UIController.setLogic(programLogic);
        UIController.populateList(programLogic.executeCommand("list"));
        primaryStage.getIcons().add(new Image("/lifetracker/icon.png"));
        primaryStage.show();
        
    }
    
    @Override
    public void stop() throws Exception{
        fileStorage.close();
    }
}
