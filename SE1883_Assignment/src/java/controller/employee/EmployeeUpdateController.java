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
public class EmployeeUpdateController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        EmployeeDBContext dbEmp = new EmployeeDBContext();
        Employee e = dbEmp.get(id);
        if (e != null) {
            DepartmentDBContext dbDept = new DepartmentDBContext();
            ArrayList<Department> depts = dbDept.list();
            req.setAttribute("e", e);
            req.setAttribute("depts", depts);
            req.getRequestDispatcher("../view/employee/update.jsp").forward(req, resp);
        } else {
            resp.sendError(404, "employee does not exist!");
        }
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {

        // Read parameters
        String raw_id = req.getParameter("id");
        String raw_name = req.getParameter("name");
        String raw_phonenumber = req.getParameter("phonenumber");
        String raw_address = req.getParameter("address");
        String raw_did = req.getParameter("did");

        // Error flag
        boolean hasError = false;

        // Validate data
        Pattern idPattern = Pattern.compile("\\d+");
        Pattern phonePattern = Pattern.compile("\\d{10,15}");

        // Validate ID
        if (raw_id == null || raw_id.isBlank() || !idPattern.matcher(raw_id).matches()) {
            req.setAttribute("errId", "Invalid or missing ID.");
            hasError = true;
        }

        // Validate name
        if (raw_name == null || raw_name.trim().isEmpty()) {
            req.setAttribute("errName", "Name must not be empty.");
            hasError = true;
        }

        // Validate phone number
        if (raw_phonenumber == null || !phonePattern.matcher(raw_phonenumber).matches()) {
            req.setAttribute("errPhoneNumber", "Phone number must be between 10 to 15 digits.");
            hasError = true;
        }

        // Validate address
        if (raw_address == null || raw_address.trim().isEmpty()) {
            req.setAttribute("errAddress", "Address must not be empty.");
            hasError = true;
        }

        // Validate department ID
        if (raw_did == null || raw_did.isBlank() || !idPattern.matcher(raw_did).matches()) {
            req.setAttribute("errDid", "Invalid department ID.");
            hasError = true;
        }

        if (hasError) {
            int id = Integer.parseInt(raw_id);
            EmployeeDBContext dbEmp = new EmployeeDBContext();
            Employee e = dbEmp.get(id);
            if (e != null) {
                DepartmentDBContext dbDept = new DepartmentDBContext();
                ArrayList<Department> depts = dbDept.list();
                req.setAttribute("e", e);
                req.setAttribute("depts", depts);
            }
            req.getRequestDispatcher("../view/employee/update.jsp").forward(req, resp);
            return;
        }

        // Object binding
        Employee e = new Employee();
        e.setId(Integer.parseInt(raw_id));
        e.setName(raw_name);
        e.setPhoneNumber(raw_phonenumber);
        e.setAddress(raw_address);

        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        e.setDept(d);

        // Save data
        EmployeeDBContext db = new EmployeeDBContext();
        db.update(e);

        // Redirect to list page after update
        resp.sendRedirect("list");
    }

}