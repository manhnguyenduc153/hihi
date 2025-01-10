/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.detail;

import controller.auth.BaseRBACController;
import dal.PlanDetailDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductPlanDetail;
import model.ProductPlanHeader;
import model.Shift;
import model.User;

/**
 *
 * @author admin
 */
public class PlanDetailUpdate extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        int pdid = Integer.parseInt(req.getParameter("pdid"));
        int plid = Integer.parseInt(req.getParameter("plid"));
        int headerId = Integer.parseInt(req.getParameter("header")); // Lấy header ID từ form
        int shiftId = Integer.parseInt(req.getParameter("shift"));   // Lấy shift ID từ form
        String date = req.getParameter("date");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // Tạo đối tượng ProductPlanDetail để cập nhật
        ProductPlanDetail detail = new ProductPlanDetail();
        detail.setId(pdid);

        // Gán ProductPlanHeader cho detail
        ProductPlanHeader header = new ProductPlanHeader();
        header.setId(headerId);
        detail.setHeader(header);

        // Gán Shift cho detail
        Shift shift = new Shift();
        shift.setSid(shiftId);
        detail.setShift(shift);

        detail.setDate(java.sql.Date.valueOf(date)); // Chuyển đổi thành kiểu Date
        detail.setQuantity(quantity);

        // Cập nhật vào cơ sở dữ liệu
        PlanDetailDBContext pddb = new PlanDetailDBContext();
        pddb.update(detail);

        // Redirect về trang chi tiết kế hoạch sản xuất
        resp.sendRedirect("listdetail?plid=" + plid);
    }

}
