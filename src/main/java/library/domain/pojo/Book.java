package library.domain.pojo;

import lombok.Getter;
import lombok.Setter;

public class Book {
    private @Setter @Getter int id;
    private @Setter @Getter Publisher publisher;
    private @Setter @Getter String title;
}
