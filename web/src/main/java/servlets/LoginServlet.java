package servlets;
import engine.Engine;
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


@WebServlet(name="LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    //login
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtils servletUtils = new ServletUtils(req);
        User newUser = (User)servletUtils.gsonFromJson(User.class);

        try {
            Engine engine = ContextServletUtils.getEngine(req);
            Traveler user = engine.login(newUser.emailAddress,newUser.password);
            resp.setHeader("travelerID", String.valueOf(user.getTravelerId()));
            servletUtils.writeJsonResponse(user);
        }catch (SQLException | Traveler.NotFoundException | Traveler.IllegalValueException e){
            servletUtils.writeJsonResponse("error", e.getMessage());
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(servletUtils.createOutResponse());
        }
    }

    static class User {
        String emailAddress;
        String password;

    }


}
