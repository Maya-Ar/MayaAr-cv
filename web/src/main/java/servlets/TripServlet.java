package servlets;

import common.DesiredHoursInDay;
import common.TripPlan;
import engine.Engine;
import engine.traveler.Traveler;
import engine.trip.DayPlan;
import engine.trip.RouteTrip;
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

@WebServlet(name = "TripServlet", urlPatterns = {"/trip", "/trip/delete", "/trip/create"})
public class TripServlet extends HttpServlet {

    @Override
    //get traveler trips
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ServletUtils servletUtils = new ServletUtils(req);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            ArrayList<TripPlan> userTrips = engine.getUserTrips();
            servletUtils.writeJsonResponse(userTrips);
        } catch (SQLException | Traveler.HasNoTripsException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getServletPath().endsWith("/delete"))
            processPostRequestDeleteTrips(req, resp);

        if (req.getServletPath().endsWith("/create"))
            processPostRequestTrips(req, resp);
    }

    //delete traveler trip
    private void processPostRequestDeleteTrips(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);
        TripsToDelete tripsToDelete = (TripsToDelete)servletUtils.gsonFromJson(TripsToDelete.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            engine.deleteTripFromUserTrips(tripsToDelete.tripsIdToDeleteList);
        } catch (SQLException | Traveler.NotFoundException | Traveler.HasNoTripsException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    //create traveler trip
    private void processPostRequestTrips(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);

        TripDetails tripDetails = (TripDetails)servletUtils.gsonFromJson(TripDetails.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            ArrayList<DayPlan> trip = engine.createTripForUser(tripDetails.destination,tripDetails.hotelID,tripDetails.mustSeenAttractionsID,tripDetails.hoursEveryDay);
            servletUtils.writeJsonResponse(trip);
        } catch (SQLException | RouteTrip.AlreadyExistException | Traveler.NotFoundException | Traveler.IllegalValueException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    @Override
    //save traveler trip
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ServletUtils servletUtils = new ServletUtils(req);

        TripPlan tripPlan = (TripPlan)servletUtils.gsonFromJson(TripPlan.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            int tripID = engine.saveUserTripOnDB(tripPlan);
            servletUtils.writeJsonResponse(tripID);
        } catch (SQLException | RouteTrip.NotFoundException | RouteTrip.AlreadyExistException | Traveler.NotFoundException e) {
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

        static class TripDetails {
            String destination;
            String hotelID;
            ArrayList<String> mustSeenAttractionsID = new ArrayList<>();
            ArrayList<DesiredHoursInDay> hoursEveryDay = new ArrayList<>();
        }

        static class TripsToDelete{
            ArrayList<String> tripsIdToDeleteList;
        }
}