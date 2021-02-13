package library.domain.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class RentForView {
    private @Getter @Setter int id;
    private @Getter @Setter int bookId;
    private @Getter @Setter String bookTitle;
    private @Getter @Setter int membershipCardId;
    private @Getter @Setter String memberName;
    private @Getter @Setter LocalDate rendDate;

    @Override
    public String toString() {
        return "RentForView{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", bookTitle='" + bookTitle + '\'' +
                ", membershipCardId=" + membershipCardId +
                ", memberName=" + memberName +
                ", rendDate=" + rendDate +
                '}';
    }
}
