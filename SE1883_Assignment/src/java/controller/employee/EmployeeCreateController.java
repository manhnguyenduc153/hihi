/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.employee;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.EmployeeDBContext;
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
import model.Employee;
import model.User;

/**
 *
 * @author admin
 */
public class EmployeeCreateController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        DepartmentDBContext db = new DepartmentDBContext();
        ArrayList<Department> depts = db.list();
        req.setAttribute("depts", depts);
        req.getRequestDispatcher("../view/employee/create.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        // Read parameters
        String raw_name = req.getParameter("name");
        String raw_phonenumber = req.getParameter("phonenumber");
        String raw_address = req.getParameter("address");
        String raw_did = req.getParameter("did");

        // Error flag
        boolean hasError = false;

        // Validate data
        if (raw_name == null || raw_name.trim().isEmpty()) {
            req.setAttribute("errName", "Name must not be empty!");
            hasError = true;
        }

        if (raw_phonenumber == null || !raw_phonenumber.matches("\\d{10,15}")) {
            req.setAttribute("errPhoneNumber", "Phone number must be between 10 to 15 digits.");
            hasError = true;
        }

        if (raw_address == null || raw_address.trim().isEmpty()) {
            req.setAttribute("errAddress", "Address must not be empty!");
            hasError = true;
        }

        if (raw_did == null || !raw_did.matches("\\d+")) {
            req.setAttribute("errDid", "Department ID must be a valid number.");
            hasError = true;
        }

        if (hasError) {
            DepartmentDBContext db = new DepartmentDBContext();
            ArrayList<Department> depts = db.list();
            req.setAttribute("depts", depts);
            req.getRequestDispatcher("../view/employee/create.jsp").forward(req, resp);
            return;
        }

        // Object binding
        Employee e = new Employee();
        e.setName(raw_name);
        e.setPhoneNumber(raw_phonenumber);
        e.setAddress(raw_address);

        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        e.setDept(d);

        // Save data
        EmployeeDBContext db = new EmployeeDBContext();
        db.insert(e);

        // Redirect to list page after creation
        resp.sendRedirect("list");
    }
}
