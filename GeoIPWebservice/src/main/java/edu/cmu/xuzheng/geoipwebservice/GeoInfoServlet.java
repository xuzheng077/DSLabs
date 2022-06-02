package edu.cmu.xuzheng.geoipwebservice;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Xu Zheng
 * @description
 */
@WebServlet(name = "GeoInfoServlet", urlPatterns = {"/GetIPGeoInfo"})
public class GeoInfoServlet extends HttpServlet {
    GeoInfoModel model = null;

    @Override
    public void init() throws ServletException {
        model = new GeoInfoModel();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ip = req.getParameter("ip");

        String result = model.lookupIp(ip);

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
