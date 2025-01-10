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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProductPlan;
import model.ProductPlanDetail;
import model.ProductPlanHeader;
import model.Shift;
import model.User;

/**
 *
 * @author admin
 */
public class PlanDetailCreate extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
// Lấy `plid` từ tham số URL
        int plid = Integer.parseInt(req.getParameter("plid"));

        // Lấy danh sách headers và shifts để hiển thị trong form
        PlanDetailDBContext dbContext = new PlanDetailDBContext();
        ArrayList<ProductPlanHeader> headers = dbContext.getHeadersByPlanId(plid); // Lấy headers theo planId
        ProductPlanDBContext pdb = new ProductPlanDBContext();
        ProductPlan plan = pdb.get(plid);
        Date startDate = plan.getStart();
        Date endDate = plan.getEnd();
        // Tạo danh sách ngày từ startDate đến endDate
        ArrayList<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        // Lặp qua các ngày từ start đến end và định dạng ngày
        while (!start.after(end)) {
            java.util.Date currentDate = start.getTime();
            String formattedDate = sdf.format(currentDate);
            dateList.add(formattedDate);
            start.add(Calendar.DATE, 1);  // Tăng thêm một ngày
        }

        // Đưa các dữ liệu vào request để hiển thị trên `createdetail.jsp`
        req.setAttribute("dateList", dateList);
        req.setAttribute("headers", headers);
        req.setAttribute("plan", plan);

        // Chuyển tiếp đến trang `createdetail.jsp`
        req.getRequestDispatcher("../view/productionPlan/createdetail.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        int plid = Integer.parseInt(req.getParameter("plid"));
        PlanDetailDBContext pddb = new PlanDetailDBContext();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày

        // Lặp qua danh sách các ngày và headers
        for (String key : req.getParameterMap().keySet()) {
            if (key.startsWith("quantity_")) { // Chỉ xử lý các tham số số lượng
                String[] parts = key.split("_");
                String currentDateString = parts[1]; // Lấy ngày
                int productId = Integer.parseInt(parts[2]); // Lấy ID sản phẩm

                // Lấy số lượng từ tham số
                String quantityParam = req.getParameter(key);
                int quantity = quantityParam != null && !quantityParam.isEmpty() ? Integer.parseInt(quantityParam) : 0;

                // Nếu số lượng lớn hơn 0, thực hiện insert
                if (quantity > 0) {
                    ProductPlanDetail detail = new ProductPlanDetail();
                    try {
                        // Chuyển đổi từ chuỗi sang java.sql.Date
                        java.sql.Date currentDate = new java.sql.Date(dateFormat.parse(currentDateString).getTime());
                        detail.setDate(currentDate); // Thiết lập ngày
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    detail.setQuantity(quantity); // Thiết lập số lượng

                    // Tìm header tương ứng để lấy thông tin Shift và Product
                    for (ProductPlanHeader header : pddb.getHeadersByPlanId(plid)) {
                        if (header.getProduct().getId() == productId) {
                            Shift shift = new Shift();
                            // Lấy SID từ key để xác định ca làm việc
                            int shiftId = Integer.parseInt(req.getParameter("shift_" + currentDateString + "_" + productId));
                            shift.setSid(shiftId);
                            shift.setSname("K" + shiftId); // Thiết lập tên Shift tương ứng
                            detail.setShift(shift); // Gán Shift cho detail

                            // Gán thông tin sản phẩm từ header
                            detail.setQuantity(quantity); // Lưu lại số lượng từ form
                            break; // Ngừng vòng lặp sau khi tìm thấy header
                        }
                    }

                    // Lưu chi tiết vào cơ sở dữ liệu
                    pddb.insert(detail);
                }
            }
        }

        // Sau khi lưu thành công, chuyển hướng về trang listdetail
        resp.sendRedirect("listdetail?plid=" + plid);
    }
}
