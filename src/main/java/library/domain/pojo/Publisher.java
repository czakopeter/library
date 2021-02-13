package library.domain.pojo;

import lombok.Getter;
import lombok.Setter;

public class Publisher {

    private @Getter @Setter int id;
    private @Getter @Setter String name;
    private @Getter @Setter String city;

    public Publisher() { }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
