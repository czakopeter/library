package library.domain.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class MembershipCard {
    private @Getter @Setter int id;
    private @Getter @Setter String name;
    private @Getter @Setter LocalDate birthdate;

    @Override
    public String toString() {
        return "MembershipCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
