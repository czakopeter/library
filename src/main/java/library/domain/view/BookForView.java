package library.domain.view;

import lombok.Getter;
import lombok.Setter;

public class BookForView {
    private @Setter @Getter String publisherName;
    private @Setter @Getter String title;

    @Override
    public String toString() {
        return "BookForView{" +
                "publisherName='" + publisherName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
