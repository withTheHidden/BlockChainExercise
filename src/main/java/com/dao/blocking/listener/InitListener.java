package com.dao.blocking.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.UUID;

@WebListener
public class InitListener implements ServletContextListener {
    static final String uuid = UUID.randomUUID().toString().replace("_","");
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("uuid",uuid);
    }
}
