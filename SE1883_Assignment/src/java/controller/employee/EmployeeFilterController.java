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
import model.Department;
import model.Employee;
import model.User;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class EmployeeFilterController extends BaseRBACController {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @param user authenticated user
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, User user)
    throws ServletException, IOException {
        String raw_id = request.getParameter("id");
        String raw_name = request.getParameter("name");
        String raw_phonenumber = request.getParameter("phonenumber");
        String raw_address = request.getParameter("address");
        String raw_did = request.getParameter("did");

        // Regular expression for validation
        Pattern idPattern = Pattern.compile("\\d+");
        Pattern phonePattern = Pattern.compile("\\d{10,15}");

        // Validate ID
        Integer id = null;
        if (raw_id != null && !raw_id.isBlank()) {
            if (idPattern.matcher(raw_id).matches()) {
                id = Integer.parseInt(raw_id);
            } else {
                request.setAttribute("errId", "Invalid ID format.");
            }
        }

        // Validate phone number
        if (raw_phonenumber != null && !raw_phonenumber.isBlank() && !phonePattern.matcher(raw_phonenumber).matches()) {
            request.setAttribute("errPhoneNumber", "Phone number must be between 10 to 15 digits.");
        }

        // Validate department ID
        Integer did = null;
        if (raw_did != null && !raw_did.equals("-1")) {
            if (idPattern.matcher(raw_did).matches()) {
                did = Integer.parseInt(raw_did);
            } else {
                request.setAttribute("errDid", "Invalid department ID format.");
            }
        }

        // Other fields are assumed to be safe text inputs but should be sanitized as needed
        String name = raw_name;
        String phonenumber = raw_phonenumber;
        String address = raw_address;

        // If there are validation errors, forward with error attributes
        if (request.getAttribute("errId") != null || request.getAttribute("errPhoneNumber") != null || request.getAttribute("errDid") != null) {
            DepartmentDBContext dbDept = new DepartmentDBContext();
            ArrayList<Department> depts = dbDept.list();
            request.setAttribute("depts", depts);
            request.getRequestDispatcher("../view/employee/filter.jsp").forward(request, response);
            return;
        }

        // Perform search if validation passes
        EmployeeDBContext dbEmp = new EmployeeDBContext();
        ArrayList<Employee> emps = dbEmp.search(id, name, phonenumber, address, did);
        DepartmentDBContext dbDept = new DepartmentDBContext();
        ArrayList<Department> depts = dbDept.list();

        request.setAttribute("depts", depts);
        request.setAttribute("emps", emps);

        request.getRequestDispatcher("../view/employee/filter.jsp").forward(request, response);
    } 

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        processRequest(req, resp, user);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        processRequest(req, resp, user);
    }

}
