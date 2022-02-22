package servlets;


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
import java.util.Collection;


@WebServlet(name = "AttractionsServlet", urlPatterns = {"/attractions/favorites", "/attractions/all", "/attractions/favorites/delete"})
public class AttractionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().endsWith("/all"))
            processGetRequestAllAttractions(req, resp);


        if (req.getServletPath().endsWith("/favorites"))
            processGetRequestFavoritesAttractions(req, resp);
    }

    //delete favorite attractions
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getServletPath().endsWith("/delete"))
            processDeleteFavoriteRequest(req, resp);
        else
            doGet(req, resp);
    }
    private void processDeleteFavoriteRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);
        FavoriteAttractions jsonFavoriteAttractions = (FavoriteAttractions) servletUtils.gsonFromJson(FavoriteAttractions.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            engine.deleteFromFavoriteAttractions(jsonFavoriteAttractions.favoriteAttractionsList);
        } catch (SQLException | Attraction.NotFoundException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }
    //get all attractions
    private void processGetRequestAllAttractions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);
        Collection<Attraction> attractions;
        try {
            Engine engine = ContextServletUtils.getEngine(req);
            attractions = engine.getAttractions(servletUtils.lines); //destination
            servletUtils.writeJsonResponse(attractions);
        } catch (SQLException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    //get all favorites attractions
    private void processGetRequestFavoritesAttractions(HttpServletRequest req, HttpServletResponse resp) throws IOException{

        ServletUtils servletUtils = new ServletUtils(req);

        ArrayList <Attraction> favoriteAttractions;

       try {
           Engine engine = ContextServletUtils.getEngine(req);
           favoriteAttractions = engine.getFavoriteAttractions();
            servletUtils.writeJsonResponse(favoriteAttractions);

        } catch (SQLException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    //add attractions to favorite attraction
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);
        FavoriteAttractions jsonFavoriteAttractions = (FavoriteAttractions) servletUtils.gsonFromJson(FavoriteAttractions.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            engine.addToFavoriteAttractions(jsonFavoriteAttractions.favoriteAttractionsList);
        } catch (SQLException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    static class FavoriteAttractions{
        ArrayList<String> favoriteAttractionsList;
    }
}

