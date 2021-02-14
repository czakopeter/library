package library.repository.jdbcTemplate;

import library.domain.save.BookForSave;
import library.domain.view.BookForView;
import library.repository.BookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookRepositoryWithJDBCTemplate extends BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepositoryWithJDBCTemplate(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BookForView save(BookForSave book) {
        String save = "insert into book (publisher_id, title) values (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(save, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, book.getPublisherId());
            stmt.setString(2, book.getTitle());
            return stmt;
        }, keyHolder);
        return findById(keyHolder.getKey().intValue());
    }

    @Override
    public List<BookForView> findAll() {
        String findAll = "SELECT * FROM book;";
        return jdbcTemplate.query(findAll, (rs, rowNum) -> convertToBookForView(rs));
    }

    @Override
    public List<String> findBookTitlesByPublisherId(int id) {
        String findTitlesOfBookByPublisherId = "SELECT title FROM book WHERE publisher_id=?";
        return jdbcTemplate.query(
                findTitlesOfBookByPublisherId,
                new Object[]{id},
                (rs, rowNum) -> rs.getString("title")
        );
    }

    private BookForView findById(int id) {
        String findById = "select * from book where id=?;";
        return jdbcTemplate.queryForObject(
                findById,
                new Object[]{id},
                (rs, rn) -> convertToBookForView(rs));
    }
}