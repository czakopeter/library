package library.repository;

import library.domain.save.BookForSave;
import library.domain.view.BookForView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookRepository {

    private final DataSource dataSource;

    public BookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BookForView save(BookForSave book) {
        String create = "INSERT INTO book (publisher_id, title) VALUES (?, ?);";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, book.getPublisherId());
            stmt.setString(2, book.getTitle());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                return findBookById(rs.getInt(1));
            } else {
                throw new IllegalStateException("Can not get generated key");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Can not create book", ex);
        }
    }

    public List<BookForView> findAll() {
        String findAll = "SELECT p.name AS name, b.title AS title FROM book b JOIN publisher p ON b.publisher_id = p.id;";
        List<BookForView> books = new LinkedList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findAll)) {


            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                books.add(convertToBookForView(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Can not create publisher", ex);
        }
        return books;
    }

    public List<String> findBookTitlesByPublisherId(int id) {
        String findAllBookByPublisherId =
                "SELECT title FROM book WHERE publisher_id=?;" ;
        List<String> titles = new LinkedList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findAllBookByPublisherId)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return titles;
    }

    private BookForView findBookById(int id) {
        String findById = "SELECT p.name AS name, b.title AS title FROM (SELECT * FROM book WHERE id=?)AS b JOIN publisher p ON b.publisher_id = p.id;";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findById)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return convertToBookForView(rs);
            } else {
                throw new IllegalArgumentException("Book does not exists with id: " + id);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected BookForView convertToBookForView(ResultSet rs) throws SQLException {
        BookForView book = new BookForView();
        book.setPublisherName(rs.getString("name"));
        book.setTitle(rs.getString("title"));
        return book;
    }
}
