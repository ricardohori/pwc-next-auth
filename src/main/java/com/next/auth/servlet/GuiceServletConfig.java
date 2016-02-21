package com.next.auth.servlet;

import com.next.auth.config.AppModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.maestrano.Maestrano;
import com.maestrano.exception.MnoConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

/**
 * User: rfh
 * Date: 19-10-2015
 *
 * Configures Guice injector (including servlet mapping) and execute app initialization
 */
@WebListener
public class GuiceServletConfig extends GuiceServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceServletConfig.class);
    private static Injector injector;

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        // Configure SSO properties default preset with the pwcsso.properties file
        try {
            Maestrano.configure("default", "pwcsso.properties");
        } catch (final MnoConfigurationException e) {
            LOGGER.error("Maestrano SSO configuration failed!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    protected Injector getInjector() {
        if(injector == null){
            injector = Guice.createInjector(new AppModule(), new ServletModule(){
                @Override
                protected void configureServlets() {
                    serve("/next/auth/saml/init").with(NextAuthSAMLServlet.class);
                    serve("/next/auth/saml/consume").with(NextAuthSAMLServlet.class);
                    serve("/next/metadata").with(NextMetadataServlet.class);
                }
            });
        }
        return injector;
    }
}