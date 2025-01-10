/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.detail;

import controller.auth.BaseRBACController;
import dal.PlanDetailDBContext;
import dal.ProductPlanDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import model.ProductPlan;
import model.ProductPlanDetail;
import model.ProductPlanHeader;
import model.Shift;
import model.User;

/**
 *
 * @author admin
 */
public class PlanDetailList extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Lấy planId từ tham số request
        int planId = Integer.parseInt(req.getParameter("plid"));

        // Sử dụng DAO để lấy danh sách chi tiết của kế hoạch sản xuất
        ProductPlanDBContext pdb = new ProductPlanDBContext();
        ProductPlan plan = pdb.get(planId);
        PlanDetailDBContext pddb = new PlanDetailDBContext();
        ArrayList<ProductPlanDetail> details = pddb.getDetailsByPlanId(planId);

        // Đưa danh sách chi tiết vào request scope
        req.setAttribute("details", details);
        req.setAttribute("plan", plan);
        // Chuyển tiếp yêu cầu tới trang JSP để hiển thị dữ liệu
        req.getRequestDispatcher("../view/productionPlan/listdetail.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
