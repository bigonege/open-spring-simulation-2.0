package org.springframework.webmvc.servlet;

import org.springframework.context.support.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 11:11
 * @Description:
 */
public class DispatchServlet extends HttpServlet {
    private final static String LOCATION_CONFIG = "contextConfigLocation";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("----------invoke doPost");;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = new ApplicationContext(config.getInitParameter(LOCATION_CONFIG));
    }
}
