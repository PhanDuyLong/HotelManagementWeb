/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.listeners;

import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author HP
 */
public class MyServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ResourceBundle rbMap = ResourceBundle.getBundle("longpd.properties.mapping");
        context.setAttribute("SITE_MAP", rbMap);
        ResourceBundle rbPer = ResourceBundle.getBundle("longpd.properties.permission");
        context.setAttribute("PER_MAP", rbPer);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
