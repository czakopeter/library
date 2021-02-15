package library.repository.jdbcTemplate;

import library.domain.pojo.Publisher;
import library.domain.save.PublisherForSave;
import library.repository.PublisherRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

public class PublisherRepositoryWithJDBCTemplate extends PublisherRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublisherRepositoryWithJDBCTemplate(DataSource dataSource) {
        super(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Publisher save(PublisherForSave publisher) {
        String create = "INSERT INTO publisher (name, city) VALUES (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getCity());
            return stmt;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).shortValue());
    }

    @Override
    public List<Publisher> findAll() {
        String findAll = "SELECT * FROM publisher;";
        return jdbcTemplate.query(findAll, (rs, rn) -> convertToPublisherForView(rs));
    }

    private Publisher findById(int id) {
        String findById = "SELECT * FROM publisher WHERE id=?;";
        return jdbcTemplate.queryForObject(
                findById, new Object[]{id}, (rs, rn) -> convertToPublisherForView(rs));
    }
}