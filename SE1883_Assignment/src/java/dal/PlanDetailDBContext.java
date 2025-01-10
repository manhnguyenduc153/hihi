/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import model.Product;
import model.ProductPlanDetail;
import model.ProductPlanHeader;
import model.Shift;

/**
 *
 * @author admin
 */
public class PlanDetailDBContext extends DBContext<ProductPlanDetail> {

    public ArrayList<ProductPlanHeader> getHeadersByPlanId(int plid) {
        ArrayList<ProductPlanHeader> headers = new ArrayList<>();
        String sql = "SELECT ph.phid, ph.plid, ph.pid, ph.quantity, p.pname "
                + "FROM PlanHeaders ph "
                + "JOIN Products p ON ph.pid = p.pid "
                + "WHERE ph.plid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, plid);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    ProductPlanHeader header = new ProductPlanHeader();
                    header.setId(rs.getInt("phid"));
                    header.setQuantity(rs.getInt("quantity")); // Lấy số lượng
                    Product product = new Product();
                    product.setId(rs.getInt("pid"));
                    product.setName(rs.getString("pname")); // Lấy tên sản phẩm
                    header.setProduct(product); // Gán sản phẩm vào header
                    headers.add(header);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return headers;
    }

    public ArrayList<ProductPlanDetail> getDetailsByPlanId(int planId) {
        ArrayList<ProductPlanDetail> details = new ArrayList<>();

        try {
            String sql = "SELECT \n"
                    + "    d.pdid, \n"
                    + "    d.phid, \n"
                    + "    d.sid, \n"
                    + "    d.date, \n"
                    + "    d.quantity, \n"
                    + "    h.pid, \n"
                    + "    p.pname, \n"
                    + "    s.sname AS shiftName\n"
                    + "FROM \n"
                    + "    PlanDetails d\n"
                    + "JOIN \n"
                    + "    PlanHeaders h ON d.phid = h.phid\n"
                    + "JOIN \n"
                    + "    Products p ON h.pid = p.pid\n"
                    + "JOIN \n"
                    + "    Shifts s ON d.sid = s.sid\n"
                    + "WHERE \n"
                    + "    h.plid = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, planId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductPlanDetail detail = new ProductPlanDetail();
                detail.setId(rs.getInt("pdid"));
                detail.setDate(rs.getDate("date"));
                detail.setQuantity(rs.getInt("quantity"));

                ProductPlanHeader header = new ProductPlanHeader();
                header.setId(rs.getInt("phid"));

                Product product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                header.setProduct(product);

                Shift shift = new Shift();
                shift.setSid(rs.getInt("sid"));
                shift.setSname(rs.getString("shiftName"));

                detail.setHeader(header);
                detail.setShift(shift);

                details.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return details;
    }

    @Override
    public void insert(ProductPlanDetail model) {
        String sql = "INSERT INTO PlanDetails (phid, sid, date, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getHeader().getId()); // phid từ header
            stm.setInt(2, model.getShift().getSid());  // sid từ shift
            stm.setDate(3, model.getDate());
            stm.setInt(4, model.getQuantity());
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(ProductPlanDetail model) {
        String sql = "UPDATE PlanDetails SET quantity = ? WHERE pdid = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getQuantity()); // Đặt giá trị mới cho quantity
            stm.setInt(2, model.getId());        // Sử dụng pdid để xác định bản ghi cần cập nhật
            stm.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(ProductPlanDetail model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ProductPlanDetail> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ProductPlanDetail get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
