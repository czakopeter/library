package library.userInterface;

import java.util.Map;

public class UserInterface {

    private boolean run;
    private final Reader reader;
    private final Map<String, MenuItem> menu;

    public UserInterface(Map<String, MenuItem> menu) {
        this.menu = menu;
        this.run = true;
        this.reader = new Reader(System.in);
    }

    public void run() {
        while (run) {
            showMenu();
            String command = reader.getString("Command: ");
            if(menu.containsKey(command)) {
                menu.get(command).getAction().run();
            } else {
                System.out.println("Invalid command");
            }
        }
    }

    public void close() {
        run = false;
    }

    private void showMenu() {
        menu.forEach((key, value) -> System.out.println("\t" + key + "\t" + value.getText()));
    }
}
