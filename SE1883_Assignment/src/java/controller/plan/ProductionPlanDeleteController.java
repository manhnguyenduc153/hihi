/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.plan;

import controller.auth.BaseRBACController;
import dal.ProductPlanDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductPlan;
import model.User;

/**
 *
 * @author admin
 */
public class ProductionPlanDeleteController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        ProductPlanDBContext pdb = new ProductPlanDBContext();
        String plid_raw = req.getParameter("plid");
        int plid = -1;
        if (plid_raw != null) {
            plid = Integer.parseInt(plid_raw);
        }

        ProductPlan plan = new ProductPlan();
        plan.setId(plid);

        pdb.delete(plan);

        resp.sendRedirect("list");
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
