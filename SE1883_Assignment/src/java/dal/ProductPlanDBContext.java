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
import model.ProductPlanDetail;
import model.ProductPlanHeader;
import model.Shift;

/**
 *
 * @author admin
 */
public class ProductPlanDBContext extends DBContext<ProductPlan> {

    @Override
    public void insert(ProductPlan model) {
        try {
            connection.setAutoCommit(false);
            String sql_insert_plan = "INSERT INTO [Plans]\n"
                    + "           ([plname]\n"
                    + "           ,[startdate]\n"
                    + "           ,[enddate]\n"
                    + "           ,[did])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";

            PreparedStatement stm_insert_plan = connection.prepareStatement(sql_insert_plan);
            stm_insert_plan.setString(1, model.getName());
            stm_insert_plan.setDate(2, model.getStart());
            stm_insert_plan.setDate(3, model.getEnd());
            stm_insert_plan.setInt(4, model.getDept().getId());
            stm_insert_plan.executeUpdate();

            String sql_select_plan = "SELECT @@IDENTITY as plid";
            PreparedStatement stm_select_plan = connection.prepareStatement(sql_select_plan);
            ResultSet rs = stm_select_plan.executeQuery();
            if (rs.next()) {
                model.setId(rs.getInt("plid"));
            }

            String sql_insert_header = "INSERT INTO [PlanHeaders]\n"
                    + "           ([plid]\n"
                    + "           ,[pid]\n"
                    + "           ,[quantity]\n"
                    + "           ,[estimatedeffort])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";

            for (ProductPlanHeader header : model.getHeaders()) {
                PreparedStatement stm_insert_header = connection.prepareStatement(sql_insert_header);
                stm_insert_header.setInt(1, model.getId());
                stm_insert_header.setInt(2, header.getProduct().getId());
                stm_insert_header.setInt(3, header.getQuantity());
                stm_insert_header.setFloat(4, header.getEstimatedeffort());
                stm_insert_header.executeUpdate();
            }

            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void update(ProductPlan model) {
        try {
            connection.setAutoCommit(false);

            // Cập nhật thông tin chính của Plan
            String sql_update_plan = "UPDATE [Plans] SET [plname] = ?, [startdate] = ?, [enddate] = ?, [did] = ? WHERE [plid] = ?";
            PreparedStatement stm_update_plan = connection.prepareStatement(sql_update_plan);
            stm_update_plan.setString(1, model.getName());
            stm_update_plan.setDate(2, model.getStart());
            stm_update_plan.setDate(3, model.getEnd());
            stm_update_plan.setInt(4, model.getDept().getId());
            stm_update_plan.setInt(5, model.getId());
            stm_update_plan.executeUpdate();

            // Câu lệnh để xóa các ProductPlanHeader có quantity và estimatedEffort đều là 0
            String sql_delete_header = "DELETE FROM [PlanHeaders] WHERE [plid] = ? AND [pid] = ?";
            String sql_update_header = "UPDATE [PlanHeaders] SET quantity = ?, estimatedeffort = ? WHERE plid = ? AND pid = ?";
            String sql_insert_header = "INSERT INTO [PlanHeaders] (plid, pid, quantity, estimatedeffort) VALUES (?, ?, ?, ?)";

            for (ProductPlanHeader header : model.getHeaders()) {
                if (header.getQuantity() == 0 && header.getEstimatedeffort() == 0) {
                    // Xóa header nếu quantity và estimatedEffort đều là 0
                    PreparedStatement stm_delete_header = connection.prepareStatement(sql_delete_header);
                    stm_delete_header.setInt(1, model.getId());
                    stm_delete_header.setInt(2, header.getProduct().getId());
                    stm_delete_header.executeUpdate();
                } else {
                    // Cập nhật header nếu quantity và estimatedEffort > 0
                    PreparedStatement stm_update_header = connection.prepareStatement(sql_update_header);
                    stm_update_header.setInt(1, header.getQuantity());
                    stm_update_header.setFloat(2, header.getEstimatedeffort());
                    stm_update_header.setInt(3, model.getId());
                    stm_update_header.setInt(4, header.getProduct().getId());

                    // Kiểm tra xem cập nhật có ảnh hưởng đến bản ghi nào không
                    int rowsAffected = stm_update_header.executeUpdate();

                    // Nếu không có bản ghi nào bị ảnh hưởng, tức là bản ghi chưa tồn tại, thì thực hiện insert
                    if (rowsAffected == 0) {
                        PreparedStatement stm_insert_header = connection.prepareStatement(sql_insert_header);
                        stm_insert_header.setInt(1, model.getId());
                        stm_insert_header.setInt(2, header.getProduct().getId());
                        stm_insert_header.setInt(3, header.getQuantity());
                        stm_insert_header.setFloat(4, header.getEstimatedeffort());
                        stm_insert_header.executeUpdate();
                    }
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(ProductPlan model) {
        try {
            connection.setAutoCommit(false);

            // Delete from PlanHeaders first
            String sql_delete_headers = "DELETE FROM [PlanHeaders] WHERE [plid] = ?";
            PreparedStatement stm_delete_headers = connection.prepareStatement(sql_delete_headers);
            stm_delete_headers.setInt(1, model.getId());
            stm_delete_headers.executeUpdate();

            // Delete from Plans
            String sql_delete_plan = "DELETE FROM [Plans] WHERE [plid] = ?";
            PreparedStatement stm_delete_plan = connection.prepareStatement(sql_delete_plan);
            stm_delete_plan.setInt(1, model.getId());
            stm_delete_plan.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<ProductPlan> list() {
        String sql = "SELECT p.plid, p.plname, p.startdate, p.enddate, d.did, d.dname, d.type, ph.phid, pr.pid, ph.quantity, ph.estimatedeffort, pr.pname, pr.description "
                + "FROM Plans p "
                + "JOIN Departments d ON p.did = d.did "
                + "JOIN PlanHeaders ph ON p.plid = ph.plid "
                + "JOIN Products pr ON ph.pid = pr.pid "
                + "ORDER BY p.plid";
        ArrayList<ProductPlan> plans = new ArrayList<>();

        PreparedStatement ps = null;

        try {

            ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            ProductPlan currentPlan = new ProductPlan();
            currentPlan.setId(-1);
            while (rs.next()) {
                int plid = rs.getInt("plid");

                if (currentPlan.getId() != plid) {

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

    @Override
    public ProductPlan get(int id) {
        ProductPlan plan = null;
        try {
            String sql = "SELECT p.plid, p.plname, p.startdate, p.enddate, "
                    + "ph.phid, prd.pname, ph.estimatedeffort, "
                    + "pd.date, pd.quantity, s.sname "
                    + "FROM Plans p "
                    + " JOIN PlanHeaders ph ON p.plid = ph.plid "
                    + " JOIN PlanDetails pd ON ph.phid = pd.phid "
                    + " JOIN Products prd ON prd.pid = ph.pid "
                    + " JOIN Shifts s ON pd.sid = s.sid "
                    + "WHERE p.plid = ?";

            try (PreparedStatement stm = connection.prepareStatement(sql)) {
                stm.setInt(1, id);

                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        if (plan == null) {
                            // Khởi tạo ProductPlan và thiết lập các thuộc tính cơ bản
                            plan = new ProductPlan();
                            plan.setId(rs.getInt("plid"));
                            plan.setName(rs.getString("plname"));
                            plan.setStart(rs.getDate("startdate"));
                            plan.setEnd(rs.getDate("enddate"));

                            // Khởi tạo danh sách headers cho kế hoạch
                            plan.setHeaders(new ArrayList<>());
                        }

                        // Tạo ProductPlanHeader nếu có
                        int phid = rs.getInt("phid");
                        if (phid != 0) {
                            ProductPlanHeader header = new ProductPlanHeader();
                            header.setId(phid);
                            header.setEstimatedeffort(rs.getFloat("estimatedeffort"));

                            // Tạo Product và gán vào header
                            Product product = new Product();
                            product.setName(rs.getString("pname"));
                            header.setProduct(product);

                            // Khởi tạo danh sách chi tiết cho header nếu chưa tồn tại
                            if (header.getDetails() == null) {
                                header.setDetails(new ArrayList<>());
                            }

                            // Tạo ProductPlanDetail
                            ProductPlanDetail detail = new ProductPlanDetail();
                            detail.setDate(rs.getDate("date"));
                            detail.setQuantity(rs.getInt("quantity"));

                            // Tạo Shift và gán vào detail
                            Shift shift = new Shift();
                            shift.setSname(rs.getString("sname"));
                            detail.setShift(shift);

                            // Thêm detail vào header
                            header.getDetails().add(detail);

                            // Kiểm tra nếu header chưa tồn tại trong danh sách headers
                            if (!plan.getHeaders().contains(header)) {
                                plan.getHeaders().add(header);
                            } else {
                                // Nếu header đã tồn tại, chỉ thêm detail vào header đó
                                int index = plan.getHeaders().indexOf(header);
                                plan.getHeaders().get(index).getDetails().add(detail);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductPlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return plan;
    }

}
