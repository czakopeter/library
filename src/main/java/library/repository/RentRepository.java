package library.repository;

import library.domain.save.RentForSave;
import library.domain.view.RentForView;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RentRepository {

    DataSource dataSource;

    public RentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RentForView save(RentForSave rent) {
        String create = "INSERT INTO rent (book_id, membership_card_id, rent_date) VALUES (?, ?, ?);";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rent.getBookId());
            stmt.setInt(2, rent.getMembershipCardId());
            stmt.setDate(3, Date.valueOf(rent.getRentDate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                return findById(rs.getInt(1));
            } else {
                throw new IllegalStateException("Can not get generated key");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Can not create rent", ex);
        }
    }
    private RentForView findById(int id) {
        String findById =
                "SELECT r.id, r.book_id, b.title, r.membership_card_id, m.name, r.rent_date " +
                        "FROM (SELECT * FROM rent WHERE id=?) AS r JOIN book b ON r.book_id = b.id " +
                        "JOIN membership_card m ON r.membership_card_id = m.id;";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findById)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return convertToRentForView(rs);
            }
            throw new IllegalArgumentException("Rent does not exists with id: " + id);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public List<RentForView> findAll() {
        List<RentForView> rents = new LinkedList<>();
        String findAll =
                "SELECT r.id, r.book_id, b.title, r.membership_card_id, m.name, r.rent_date " +
                        "FROM rent r JOIN book b ON r.book_id = b.id " +
                        "JOIN membership_card m ON r.membership_card_id = m.id;";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findAll)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rents.add(convertToRentForView(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return rents;
    }

    private RentForView convertToRentForView(ResultSet rs) throws SQLException {
        RentForView rent = new RentForView();
        rent.setId(rs.getInt("id"));
        rent.setBookId(rs.getInt("book_id"));
        rent.setBookTitle(rs.getString("title"));
        rent.setMembershipCardId(rs.getInt("membership_card_id"));
        rent.setMemberName(rs.getString("name"));
        rent.setRendDate(rs.getDate("rent_date").toLocalDate());
        return rent;
    }
}
