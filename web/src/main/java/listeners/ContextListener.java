package listeners;

import engine.Engine;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized");
        // create instance of engine
        Engine engine = new Engine();
        servletContextEvent.getServletContext().setAttribute("engine", engine);
    }
}
