package lifetracker;

import lifetracker.UI.UI;
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

public class LifeTracker {

    private static final String LOG_FOLDER = "logs/";
    private static final String LOG_FILE = "lifetracker.log";

    public static void main(String args[]) throws Exception {

        try (Storage fileStorage = new ThreadedFileStorage()) {

            setLogger();
            Parser commandParser = new ParserImpl(new CommandFactoryImpl());
            Logic programLogic = new LogicImpl(commandParser, fileStorage);

            new UI(programLogic);
        }
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
}
