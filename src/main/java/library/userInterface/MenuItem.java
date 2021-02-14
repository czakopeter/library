package library.userInterface;

import lombok.Getter;
import lombok.Setter;

public class MenuItem {

    private @Getter @Setter String text;
    private @Getter @Setter Run action;

    public MenuItem(String text, Run action) {
        this.text = text;
        this.action = action;
    }

    public interface Run {
        void run();
    }
}
