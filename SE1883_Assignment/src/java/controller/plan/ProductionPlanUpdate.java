/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.plan;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.ProductDBContext;
import dal.ProductPlanDBContext;
import dal.ProductPlanHeaderDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Pattern;
import model.Department;
import model.Product;
import model.ProductPlan;
import model.ProductPlanHeader;
import model.User;

/**
 *
 * @author admin
 */
public class ProductionPlanUpdate extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        ProductPlanHeaderDBContext pdb = new ProductPlanHeaderDBContext();
        DepartmentDBContext dbDept = new DepartmentDBContext();
        ProductDBContext dbProduct = new ProductDBContext();
        String plid_raw = req.getParameter("plid");
        int plid = -1;
        if (plid_raw != null) {
            try {
                plid = Integer.parseInt(plid_raw);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid plan ID.");
                return;
            }
        }

        req.setAttribute("plan", pdb.listHeaders(plid).get(0));
        req.setAttribute("depts", dbDept.get("Production"));
        req.setAttribute("products", dbProduct.list());

        req.getRequestDispatcher("../view/productionPlan/update.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        ProductPlan plan = new ProductPlan();
        boolean hasError = false;
        StringBuilder errorMessage = new StringBuilder();

        // Validate plan ID
        try {
            plan.setId(Integer.parseInt(req.getParameter("plid")));
        } catch (NumberFormatException e) {
            errorMessage.append("Invalid plan ID.\n");
            hasError = true;
        }

        // Validate name
        String name = req.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            errorMessage.append("Plan name must not be empty.\n");
            hasError = true;
        } else {
            plan.setName(name);
        }

        // Validate dates
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = Date.valueOf(req.getParameter("from"));
            toDate = Date.valueOf(req.getParameter("to"));
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
            d.setId(Integer.parseInt(req.getParameter("did")));
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
                int quantity = 0;
                float effort = 0;

                try {
                    quantity = (raw_quantity != null && !raw_quantity.isEmpty()) ? Integer.parseInt(raw_quantity) : 0;
                } catch (NumberFormatException e) {
                    errorMessage.append("Invalid quantity for product ID " + pid + ".\n");
                    hasError = true;
                }

                try {
                    effort = (raw_effort != null && !raw_effort.isEmpty()) ? Float.parseFloat(raw_effort) : 0;
                } catch (NumberFormatException e) {
                    errorMessage.append("Invalid effort for product ID " + pid + ".\n");
                    hasError = true;
                }

                header.setQuantity(quantity);
                header.setEstimatedeffort(effort);

                if (quantity > 0 && effort > 0) {
                    plan.getHeaders().add(header);
                }
            }
        }

        if (plan.getHeaders().isEmpty()) {
            errorMessage.append("Your plan must have at least one valid header with positive quantity and effort.\n");
            hasError = true;
        }

        if (hasError) {
            // Keep the user on the update page and display error messages
            req.setAttribute("errorMessage", errorMessage.toString());
            req.setAttribute("plan", plan);
            DepartmentDBContext dbDept = new DepartmentDBContext();
            ProductDBContext dbProduct = new ProductDBContext();
            req.setAttribute("depts", dbDept.get("Production"));
            req.setAttribute("products", dbProduct.list());
            req.getRequestDispatcher("../view/productionPlan/update.jsp").forward(req, resp);
            return;
        }

        // Save data if validation passes
        ProductPlanDBContext db = new ProductPlanDBContext();
        db.update(plan);
        resp.sendRedirect("list");
    }
}
