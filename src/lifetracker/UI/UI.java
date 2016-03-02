package lifetracker.UI;

import lifetracker.logic.Logic;

public interface UI {
	
	void welcomeMessage();
	
	void executeUntilExit(Logic l);
}
