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
import model.Department;
import model.Product;
import model.ProductPlan;
import model.ProductPlanHeader;

/**
 *
 * @author admin
 */
public class ProductPlanHeaderDBContext extends DBContext<ProductPlanHeader>{
    
    @Override
    public void insert(ProductPlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(ProductPlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(ProductPlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ProductPlanHeader> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ProductPlanHeader get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public ArrayList<ProductPlan> listHeaders(int plid) {
                String sql = "SELECT p.plid, p.plname, p.startdate, p.enddate, d.did, d.dname, d.type, ph.phid, pr.pid, ph.quantity, ph.estimatedeffort, pr.pname, pr.description "
                + "FROM Plans p "
                + "JOIN Departments d ON p.did = d.did "
                + "JOIN PlanHeaders ph ON p.plid = ph.plid "
                + "JOIN Products pr ON ph.pid = pr.pid "
                + "where p.plid = ?";
        ArrayList<ProductPlan> plans = new ArrayList<>();

        PreparedStatement ps = null;

        try {

            ps = connection.prepareStatement(sql);
            ps.setInt(1, plid);
            ResultSet rs = ps.executeQuery();

            ProductPlan currentPlan = new ProductPlan();
            currentPlan.setId(-1);
            while (rs.next()) {
                int currentPlid = rs.getInt("plid");

                if (currentPlan.getId() != currentPlid) {

                    // Create a new ProductionPlan object
                    currentPlan = new ProductPlan();
                    currentPlan.setId(plid);
                    currentPlan.setName(rs.getString("plname"));
                    currentPlan.setStart(rs.getDate("startdate"));
                    currentPlan.setEnd(rs.getDate("enddate"));

                    // Create and set Department object
                    Department department = new Department();
                    department.setId(rs.getInt("did"));
                    department.setName(rs.getString("dname"));
                    department.setType(rs.getString("type"));
                    currentPlan.setDept(department);
                    plans.add(currentPlan);
                    currentPlan.setHeaders(new ArrayList<>());
                }

                // Check if PlanHeader exists
                int phid = rs.getInt("phid");
                if (phid != 0) {
                    // Create a new PlanHeader object and add it to the current plan
                    ProductPlanHeader header = new ProductPlanHeader();
                    header.setId(phid);
                    header.setQuantity(rs.getInt("quantity"));
                    header.setEstimatedeffort(rs.getFloat("estimatedeffort"));

                    // Create a new Product object and set it in the header
                    Product product = new Product();
                    product.setId(rs.getInt("pid"));
                    product.setName(rs.getString("pname"));
                    product.setDescription(rs.getString("description"));
                    header.setProduct(product);
                    header.setPlan(currentPlan);

                    // Add the header to the current plan
                    currentPlan.getHeaders().add(header);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return plans;
    }
}
