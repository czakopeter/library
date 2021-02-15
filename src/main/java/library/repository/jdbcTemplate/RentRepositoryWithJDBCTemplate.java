package library.repository.jdbcTemplate;

import library.domain.save.RentForSave;
import library.domain.view.RentForView;
import library.repository.RentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.security.Key;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

public class RentRepositoryWithJDBCTemplate extends RentRepository {

    private final JdbcTemplate jdbcTemplate;

    public RentRepositoryWithJDBCTemplate(DataSource dataSource) {
        super(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public RentForView save(RentForSave rent) {
        String create = "INSERT INTO rent (book_id, membership_card_id, rent_date) VALUES (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, rent.getBookId());
            stmt.setInt(2, rent.getMembershipCardId());
            stmt.setDate(3, Date.valueOf(rent.getRentDate()));
            return stmt;
        });

        return findById(keyHolder.getKey().intValue());
    }

    @Override
    public List<RentForView> findAll() {
        String findAll =
                "SELECT r.id, r.book_id, b.title, r.membership_card_id, m.name, r.rent_date " +
                        "FROM rent r JOIN book b ON r.book_id = b.id " +
                        "JOIN membership_card m ON r.membership_card_id = m.id;";
        return jdbcTemplate.query(findAll, (rs, rn) -> convertToRentForView(rs));
    }

    private RentForView findById(int id) {
        String findById =
                "SELECT r.id, r.book_id, b.title, r.membership_card_id, m.name, r.rent_date " +
                        "FROM (SELECT * FROM rent WHERE id=?) AS r JOIN book b ON r.book_id = b.id " +
                        "JOIN membership_card m ON r.membership_card_id = m.id;";
        return jdbcTemplate.queryForObject(
                findById, new Object[]{id}, (rs, rn) -> convertToRentForView(rs));
    }
}
