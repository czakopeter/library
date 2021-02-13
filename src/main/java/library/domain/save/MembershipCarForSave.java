package library.domain.save;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class MembershipCarForSave {

    private @Getter @Setter String name;
    private @Getter @Setter LocalDate birthdate;
}
