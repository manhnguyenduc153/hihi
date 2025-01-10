/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AccessFilter implements Filter {
    private Set<String> allowedUrls = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Đọc file allowed_access.txt để lấy danh sách URL được phép
        String filePath = filterConfig.getServletContext().getRealPath("/WEB-INF/allowed_access.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allowedUrls.add(line.trim());
            }
        } catch (IOException e) {
            throw new ServletException("Could not load allowed access file", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Kiểm tra nếu URL có trong danh sách cho phép
        if (allowedUrls.contains(requestUri)) {
            chain.doFilter(request, response); // Cho phép tiếp tục
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access to this URL is not allowed.");
        }
    }

    @Override
    public void destroy() {
        // Cleanup nếu cần thiết
    }
}
