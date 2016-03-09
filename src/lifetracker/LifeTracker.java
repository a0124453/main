package lifetracker;

import lifetracker.UI.UI;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicImpl;
import lifetracker.parser.Parser;
import lifetracker.parser.ParserImpl;
import lifetracker.storage.Storage;
import lifetracker.storage.ThreadedFileStorage;

public class LifeTracker {

    public static void main(String args[]) throws Exception {

        try (Storage fileStorage = new ThreadedFileStorage()) {

            Parser commandParser = new ParserImpl();

            Logic programLogic = new LogicImpl(commandParser, fileStorage);

            new UI(programLogic);
        }
    }
}
