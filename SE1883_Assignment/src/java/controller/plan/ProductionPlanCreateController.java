/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.plan;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.ProductDBContext;
import dal.ProductPlanDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import model.Department;
import model.Product;
import model.ProductPlan;
import model.ProductPlanHeader;
import java.util.regex.Pattern;
import model.User;

/**
 *
 * @author admin
 */
public class ProductionPlanCreateController extends BaseRBACController {


    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        DepartmentDBContext dbDept = new DepartmentDBContext();
        ProductDBContext dbProduct = new ProductDBContext();

        req.setAttribute("depts", dbDept.get("Production"));
        req.setAttribute("products", dbProduct.list());

        req.getRequestDispatcher("../view/productionPlan/create.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        ProductPlan plan = new ProductPlan();
        String raw_name = req.getParameter("name");
        String raw_from = req.getParameter("from");
        String raw_to = req.getParameter("to");
        String raw_did = req.getParameter("did");

        boolean hasError = false;
        StringBuilder errorMessage = new StringBuilder();

        // Validate name
        if (raw_name == null || raw_name.trim().isEmpty()) {
            errorMessage.append("Name must not be empty.\n");
            hasError = true;
        } else {
            plan.setName(raw_name);
        }

        // Validate date
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = Date.valueOf(raw_from);
            toDate = Date.valueOf(raw_to);
            if (toDate.before(fromDate)) {
                errorMessage.append("End date cannot be earlier than start date.\n");
                hasError = true;
            } else {
                plan.setStart(fromDate);
                plan.setEnd(toDate);
            }
        } catch (IllegalArgumentException e) {
            errorMessage.append("Invalid date format.\n");
            hasError = true;
        }

        // Validate department ID
        Department d = new Department();
        try {
            d.setId(Integer.parseInt(raw_did));
            plan.setDept(d);
        } catch (NumberFormatException e) {
            errorMessage.append("Invalid department ID.\n");
            hasError = true;
        }

        // Validate product plan headers
        String[] pids = req.getParameterValues("pid");
        if (pids != null) {
            for (String pid : pids) {
                Product p = new Product();
                try {
                    p.setId(Integer.parseInt(pid));
                } catch (NumberFormatException e) {
                    continue; // Skip invalid product IDs
                }

                ProductPlanHeader header = new ProductPlanHeader();
                header.setProduct(p);

                String raw_quantity = req.getParameter("quantity" + pid);
                String raw_effort = req.getParameter("effort" + pid);

                try {
                    header.setQuantity(raw_quantity != null && raw_quantity.length() > 0 ? Integer.parseInt(raw_quantity) : 0);
                } catch (NumberFormatException e) {
                    header.setQuantity(0); // Default to 0 if invalid
                }

                try {
                    header.setEstimatedeffort(raw_effort != null && raw_effort.length() > 0 ? Float.parseFloat(raw_effort) : 0);
                } catch (NumberFormatException e) {
                    header.setEstimatedeffort(0); // Default to 0 if invalid
                }

                if (header.getQuantity() > 0 && header.getEstimatedeffort() > 0) {
                    plan.getHeaders().add(header);
                }
            }
        }

        if (plan.getHeaders().isEmpty()) {
            errorMessage.append("Your plan must have at least one valid header with positive quantity and effort.\n");
            hasError = true;
        }

        if (hasError) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Validation errors:\n" + errorMessage.toString());
            return;
        }

        // Save data if validation passes
        ProductPlanDBContext db = new ProductPlanDBContext();
        db.insert(plan);
        resp.sendRedirect("list");
    }

}
