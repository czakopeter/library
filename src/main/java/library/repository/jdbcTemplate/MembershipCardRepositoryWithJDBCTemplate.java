package library.repository.jdbcTemplate;

import library.domain.pojo.MembershipCard;
import library.domain.save.MembershipCarForSave;
import library.repository.MembershipCardRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

public class MembershipCardRepositoryWithJDBCTemplate extends MembershipCardRepository {

    private final JdbcTemplate jdbcTemplate;

    public MembershipCardRepositoryWithJDBCTemplate(DataSource dataSource) {
        super(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public MembershipCard save(MembershipCarForSave card) {
        String create = "INSERT INTO membership_card (name, birthdate) VALUES (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement stml = conn.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS);
            stml.setString(1, card.getName());
            stml.setDate(2, Date.valueOf(card.getBirthdate()));
            return stml;
        });

        MembershipCard view = new MembershipCard();
        view.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        view.setName(card.getName());
        view.setBirthdate(card.getBirthdate());
        return view;
    }

    @Override
    public List<MembershipCard> findAll() {
        String findAll = "SELECT * FROM membership_card;";
        return jdbcTemplate.query(findAll, (rs, rn) -> convertToMembershipCardForView(rs));
    }
}
