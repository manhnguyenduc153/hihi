/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author admin
 */
public class DateTag extends SimpleTagSupport {

    private Date value;

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public void doTag() throws JspException, IOException {
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=ProductionSchedulingSystem;trustServerCertificate=true;";
        String user = "hayden";
        String password = "1234";
        if (value != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd 'thang' MM 'nam' yyyy");
            String formattedDate = "ngay " + formatter.format(value);
            JspWriter out = getJspContext().getOut();
            out.print(formattedDate);
        }
    }
}
