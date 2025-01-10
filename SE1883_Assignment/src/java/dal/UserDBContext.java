/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Feature;
import model.Role;
import model.User;

/**
 *
 * @author admin
 */
public class UserDBContext extends DBContext<User> {

    public ArrayList<Role> getRoles(String username) {
        PreparedStatement stm = null;
        ArrayList<Role> roles = new ArrayList<>();
        try {
            String sql = "SELECT r.rid, r.rname, f.fid, f.fname, f.url FROM [Users] u\n"
                    + "JOIN UserRoles ur \n"
                    + "ON u.username = ur.username\n"
                    + "JOIN [Roles] r\n"
                    + "ON r.rid = ur.rid\n"
                    + "JOIN RoleFeature rf\n"
                    + "ON rf.rid = r.rid\n"
                    + "JOIN Features f\n"
                    + "ON f.fid = rf.fid\n"
                    + "WHERE u.username = ?";

            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            Role crole = new Role();
            crole.setRid(-1);

            while (rs.next()) {
                int rid = rs.getInt("rid");
                if (rid != crole.getRid()) {
                    crole = new Role();
                    crole.setRid(rid);
                    crole.setRname(rs.getString("rname"));
                    roles.add(crole);
                }

                Feature feature = new Feature();
                feature.setFid(rs.getInt("fid"));
                feature.setFname(rs.getString("fname"));
                feature.setUrl(rs.getString("url"));
                feature.setRoles(roles);
                crole.getFeatures().add(feature);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return roles;
    }

    public User get(String username, String password) {
        User user = null;
        PreparedStatement stm = null;
        try {
            String sql = "SELECT [username],[password] FROM [Users]\n"
                    + "WHERE [username] = ? AND [password] = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUsername(username);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }

    @Override
    public void insert(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<User> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
