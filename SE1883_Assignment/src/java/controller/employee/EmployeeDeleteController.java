/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.employee;

import controller.auth.BaseRBACController;
import dal.EmployeeDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import model.User;

/**
 *
 * @author admin
 */
public class EmployeeDeleteController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        resp.sendError(403, "You are not allowed to access this feature by this way!");
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        EmployeeDBContext db = new EmployeeDBContext();
        Employee e = new Employee();
        e.setId(id);
        db.delete(e);
        resp.sendRedirect("list");
    }

}
