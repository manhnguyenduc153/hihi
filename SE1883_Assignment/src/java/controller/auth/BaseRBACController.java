/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.auth;

import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import model.Feature;
import model.Role;
import model.User;

/**
 *
 * @author admin
 */
public abstract class BaseRBACController extends BaseRequiredAuthenticationController {

    private void grantAccessControls(User account, HttpServletRequest req) {
        UserDBContext db = new UserDBContext();
        ArrayList<Role> roles = db.getRoles(account.getUsername());
        account.setRoles(roles);
        req.getSession().setAttribute("account", account);
    }

    private boolean isAuthorize(User account, HttpServletRequest req) {
        grantAccessControls(account, req);
        String url = req.getServletPath();
        for (Role role : account.getRoles()) {
            for (Feature feature : role.getFeatures()) {
                if (feature.getUrl().equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        if (isAuthorize(user, req)) {
            doAuthorizedPost(req, resp, user);
        } else {
            resp.sendError(403, "You cannot acces this page!!!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        if (isAuthorize(user, req)) {
            doAuthorizedGet(req, resp, user);
        } else {
            resp.sendError(403, "You cannot acces this page!!!");
        }
    }

    protected abstract void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;

    protected abstract void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;
}
