package library.repository;

import com.mysql.cj.jdbc.MysqlDataSource;
import library.domain.pojo.MembershipCard;
import library.domain.save.MembershipCarForSave;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MembershipCardRepository {

    DataSource dataSource;

    public MembershipCardRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MembershipCard save(MembershipCarForSave card) {
        String create = "INSERT INTO membership_card (name, birthdate) VALUES (?, ?);";
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, card.getName());
            stmt.setDate(2, Date.valueOf(card.getBirthdate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                MembershipCard result = new MembershipCard();
                result.setId(rs.getInt(1));
                result.setName(card.getName());
                result.setBirthdate(card.getBirthdate());
                return result;
            } else {
                throw new IllegalStateException("Can not get generated key");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Can not create membership card", ex);
        }
    }

    public List<MembershipCard> findAll() {
        String findAll = "SELECT * FROM membership_card;";
        List<MembershipCard> cards = new LinkedList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement stmt = c.prepareStatement(findAll)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                cards.add(convertToMembershipCardForView(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return cards;
    }

    protected MembershipCard convertToMembershipCardForView(ResultSet rs) throws SQLException {
        MembershipCard card = new MembershipCard();
        card.setId(rs.getInt("id"));
        card.setName(rs.getString("name"));
        card.setBirthdate(rs.getDate("birthdate").toLocalDate());
        return card;
    }
}
