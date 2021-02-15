package library.userInterface;

import java.time.LocalDate;
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

    public String getString(String question) {
        return reader.getString(question);
    }

    public int getInt(String question, String invalid) {
        return reader.getInt(question, invalid);
    }

    public LocalDate getDate(String question) {
        return reader.getDate(question);
    }

    private void showMenu() {
        menu.forEach((key, value) -> System.out.println("\t" + key + "\t" + value.getText()));
    }
}
