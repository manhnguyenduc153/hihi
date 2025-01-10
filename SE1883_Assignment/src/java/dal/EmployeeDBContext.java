/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Department;
import model.Employee;

/**
 *
 * @author admin
 */
public class EmployeeDBContext extends DBContext<Employee> {

    public ArrayList<Employee> search(Integer id, String name, String phonenumber, String address, Integer did) {
        String sql = "SELECT e.eid,e.ename,e.phonenumber,e.address,d.did,d.dname FROM Employees e \n"
                + "INNER JOIN Departments d ON d.did = e.did\n"
                + "WHERE (1=1)";
        ArrayList<Employee> emps = new ArrayList<>();
        ArrayList<Object> paramValues = new ArrayList<>();

        if (id != null) {
            sql += " AND e.eid = ?";
            paramValues.add(id);
        }

        if (name != null) {
            sql += " AND e.ename LIKE '%'+?+'%'";
            paramValues.add(name);
        }

        if (phonenumber != null) {
            sql += " AND e.phonenumber LIKE '%' + ? + '%'";
            paramValues.add(phonenumber);
        }

        if (address != null) {
            sql += " AND e.address LIKE '%' + ? + '%'";
            paramValues.add(address);
        }

        if (did != null) {
            sql += " AND e.did = ?";
            paramValues.add(did);
        }

        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sql);
            for (int i = 0; i < paramValues.size(); i++) {
                stm.setObject((i + 1), paramValues.get(i));
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setPhoneNumber(rs.getNString("phonenumber"));
                e.setAddress(rs.getString("address"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);
                emps.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return emps;
    }

    @Override
    public void insert(Employee model) {
        String sql_insert = ""
                + "INSERT INTO [Employees]\n"
                + "           ([ename]\n"
                + "           ,[did]\n"
                + "           ,[phonenumber]\n"
                + "           ,[address])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?)\n";

        PreparedStatement stm_insert = null;

        try {
            connection.setAutoCommit(false);
            stm_insert = connection.prepareStatement(sql_insert);
            stm_insert.setString(1, model.getName());
            stm_insert.setInt(2, model.getDept().getId());
            stm_insert.setString(3, model.getPhoneNumber());
            stm_insert.setString(4, model.getAddress());

            stm_insert.executeUpdate();

            ResultSet rs = stm_insert.getGeneratedKeys();
            if (rs.next()) {
                model.setId(rs.getInt(1));
            }
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                if (stm_insert != null) {
                    stm_insert.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Employee model) {
        String sql_update = "UPDATE [Employees]\n"
                + "   SET [ename] = ?\n"
                + "      ,[did] = ?\n"
                + "      ,[phonenumber] = ?\n"
                + "      ,[address] = ?\n"
                + " WHERE eid = ?";
        PreparedStatement stm_update = null;
        try {

            stm_update = connection.prepareStatement(sql_update);
            stm_update.setString(1, model.getName());
            stm_update.setInt(2, model.getDept().getId());
            stm_update.setString(3, model.getPhoneNumber());
            stm_update.setString(4, model.getAddress());
            stm_update.setInt(5, model.getId());
            stm_update.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(Employee model) {
        String sql_update = "DELETE FROM Employees\n"
                + " WHERE eid = ?";
        PreparedStatement stm_update = null;
        try {

            stm_update = connection.prepareStatement(sql_update);
            stm_update.setInt(1, model.getId());
            stm_update.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<Employee> list() {
        ArrayList<Employee> emps = new ArrayList<>();
        PreparedStatement stm = null;
        try {
            String sql = "SELECT [eid]\n"
                    + "      ,[ename]\n"
                    + "      ,[phonenumber]\n"
                    + "      ,[address]\n"
                    + "  FROM [Employees]";
            stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setPhoneNumber(rs.getString("phonenumber"));
                e.setAddress(rs.getString("address"));
                emps.add(e);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return emps;
    }

    @Override
    public Employee get(int id) {
        PreparedStatement stm = null;
        try {
            String sql = "SELECT \n"
                    + "    e.eid, e.ename, e.phonenumber, e.[address],\n"
                    + "    e.did, d.dname\n"
                    + "FROM Employees e \n"
                    + "INNER JOIN Departments d ON d.did = e.did\n"
                    + "WHERE e.eid = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setPhoneNumber(rs.getString("phonenumber"));
                e.setAddress(rs.getString("address"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);

                return e;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
