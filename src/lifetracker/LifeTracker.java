package lifetracker;

import lifetracker.UI.UI;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicImpl;
import lifetracker.parser.Parser;
import lifetracker.parser.ParserImpl;
import lifetracker.storage.Storage;
import lifetracker.storage.ThreadedFileStorage;

import java.io.IOException;

public class LifeTracker {

    public static void main(String args[]) {

        Storage fileStorage = createStorage();

        Parser commandParser = new ParserImpl();

        Logic programLogic = new LogicImpl(commandParser, fileStorage);

        new UI(programLogic);
    }

    private static Storage createStorage(){
        Storage fileStorage = null;

        try {
            fileStorage = new ThreadedFileStorage();

            return fileStorage;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return fileStorage;
    }
}
