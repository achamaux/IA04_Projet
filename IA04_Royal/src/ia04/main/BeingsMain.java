package ia04.main;

import ia04.model.Beings;
import sim.display.Console;
import ia04.gui.BeingsWithUI;

public class BeingsMain {
	public static void main(String[] args) {
        runUI();
	}
	public static void runUI() {
		Beings model = new Beings(System.currentTimeMillis());
		BeingsWithUI gui = new BeingsWithUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
