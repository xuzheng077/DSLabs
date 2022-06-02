package edu.cmu.xuzheng.geoipwebservicelogging;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * @author Xu Zheng
 * @description
 */
@WebServlet(name = "GeoInfoDashBoardServlet", urlPatterns = {"/GetIPGeoInfoDashBoard"})
public class DashBoardServlet extends HttpServlet {

    private DashBoardModel model;

    @Override
    public void init() throws ServletException {
        model = new DashBoardModel();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<JSONObject> logs = model.getAllLogs();
        AnalyticResult result = model.getAnalytics(logs);
        req.setAttribute("logs", logs);
        req.setAttribute("analytics", result);
        // Pass the user search string (pictureTag) also to the view.
        String nextView = "index.jsp";

        // Transfer control over the the correct "view"
        RequestDispatcher view = req.getRequestDispatcher(nextView);
        view.forward(req, resp);
    }
}
