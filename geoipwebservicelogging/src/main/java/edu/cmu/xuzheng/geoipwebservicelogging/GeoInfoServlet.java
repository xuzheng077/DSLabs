package edu.cmu.xuzheng.geoipwebservicelogging;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Xu Zheng
 * @description 1. android model
 * 2. parameter 'ip' sent by android
 * 3. timestamp when the request from android is received
 * 4. timestamp when the request is sent to 3rd API
 * 5. timestamp when the request is received from 3rd API
 * 5. reply data from the 3rd API
 * 6. timestamp when the reply is sent to Android
 * 7. user ip
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
        String userDevice = req.getHeader("User-Agent");
        String userIP = req.getRemoteAddr();
        String result = model.lookupIp(ip, userDevice, userIP);

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
