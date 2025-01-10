/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author admin
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) sr;
        HttpServletResponse httpResponse = (HttpServletResponse) sr1;
        HttpSession session = httpRequest.getSession();

        // Kiểm tra session xem người dùng có đăng nhập hay chưa
        if (session == null || session.getAttribute("account") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        } else {
            fc.doFilter(sr, sr1); // Cho phép tiếp tục nếu đã đăng nhập
        }
    }

}
