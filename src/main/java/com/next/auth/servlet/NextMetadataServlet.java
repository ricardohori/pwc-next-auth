package com.next.auth.servlet;

import com.maestrano.Maestrano;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * User: rfh
 * Date: 18-02-2016
 *
 * </pre>
 */
@Singleton
public class NextMetadataServlet extends HttpServlet {

    private static final String APPLICATION_JSON = "application/json";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(APPLICATION_JSON);
        resp.getWriter().print(Maestrano.getDefault().toMetadata());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
