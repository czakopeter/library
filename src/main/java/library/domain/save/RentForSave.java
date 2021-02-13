package library.domain.save;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class RentForSave {
    private @Getter @Setter int bookId;
    private @Getter @Setter int membershipCardId;
    private @Getter @Setter LocalDate rentDate;

}
