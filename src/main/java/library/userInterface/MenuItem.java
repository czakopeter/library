package library.userInterface;

import lombok.Getter;

public class MenuItem {

    private final @Getter String text;
    private final @Getter Run action;

    public MenuItem(String text, Run action) {
        this.text = text;
        this.action = action;
    }

    public interface Run {
        void run();
    }
}
