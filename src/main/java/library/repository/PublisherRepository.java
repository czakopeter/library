package library.repository;

import library.domain.pojo.Publisher;
import library.domain.save.PublisherForSave;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PublisherRepository {

    private final DataSource dataSource;

    public PublisherRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Publisher save(PublisherForSave publisher) {
        String create = "INSERT INTO publisher (name, city) VALUES (?, ?);";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, publisher.getName());
            stmt.setString(2, publisher.getCity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                Publisher result = new Publisher();
                result.setId(rs.getInt(1));
                result.setName(publisher.getName());
                result.setCity(publisher.getCity());
                return result;
            } else {
                throw new IllegalStateException("Can not get generated key");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Can not create publisher", ex);
        }
    }

    public List<Publisher> findAll() {
        String findAll = "SELECT * FROM publisher;";
        List<Publisher> publishers = new LinkedList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findAll)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                publishers.add(convertToPublisherForView(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return publishers;
    }

    protected Publisher convertToPublisherForView(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher();
        publisher.setId(rs.getInt("id"));
        publisher.setName(rs.getString("name"));
        publisher.setCity(rs.getString("city"));
        return publisher;
    }
}
