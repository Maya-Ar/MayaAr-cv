package servlets;

import common.Destinations;
import engine.Engine;
import engine.attraction.Attraction;
import engine.traveler.Traveler;
import servlets.utils.ContextServletUtils;
import servlets.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "HotelsServlet", urlPatterns = {"/hotels"})
public class HotelsServlet extends HttpServlet {

    @Override
    //get hotels
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils servletUtils = new ServletUtils(req);

        ArrayList<Attraction> hotels;
        try {
            Engine engine = ContextServletUtils.getEngine(req);
            String destination = servletUtils.lines;
            Destinations.valueOf(destination);
            hotels = engine.getHotelsByDestination(destination);
            servletUtils.writeJsonResponse(hotels);
        } catch (SQLException | Attraction.NoHotelsOnDestination | Traveler.NotFoundException | IllegalArgumentException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }


}
