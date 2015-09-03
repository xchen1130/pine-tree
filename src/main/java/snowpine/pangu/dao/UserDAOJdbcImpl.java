/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snowpine.pangu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author Xuesong
 */
public class UserDAOJdbcImpl implements UserDAO {

    private static final String SQL_GET_USER = "select * from users where user_id=?";
    private static final String SQL_GET_USER_BY_EMAIL = "select * from users where email=?";
    private static final String SQL_GET_BALANCE = "select balance from users where user_id=?";

    private final DataSource dataSource;

    public UserDAOJdbcImpl(DataSource ds) {
        this.dataSource = ds;
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setBalance(rs.getLong("balance"));
        user.setEmail(rs.getString("email"));
        user.setSalt(rs.getString("salt"));
        user.setPasshash(rs.getString("passhash"));

        return user;
    }

    @Override
    public User findById(long id) throws DAOWrapperException {
        User user = null;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_GET_USER);) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                user = getUser(rs);
            }

        } catch (SQLException sqle) {
            DAOWrapperException.printSQLException(sqle);
            throw new DAOWrapperException("SQLException", sqle);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) throws DAOWrapperException {
        User user = null;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_GET_USER_BY_EMAIL);) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                user = getUser(rs);
            }

        } catch (SQLException sqle) {
            DAOWrapperException.printSQLException(sqle);
            throw new DAOWrapperException("SQLException", sqle);
        }

        return user;
    }

    @Override
    public long getBalanceById(long id) throws DAODataException, DAOWrapperException {
        
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_GET_BALANCE);) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return  rs.getLong("balance");
            } else {
                throw new DAODataException("user " + id + " does not exist!");
            }

        } catch (SQLException sqle) {
            DAOWrapperException.printSQLException(sqle);
            throw new DAOWrapperException("SQLException", sqle);
        }
    }
}
