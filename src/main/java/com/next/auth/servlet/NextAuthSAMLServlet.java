package com.next.auth.servlet;

import com.maestrano.Maestrano;
import com.maestrano.saml.AuthRequest;
import com.maestrano.saml.Response;
import com.maestrano.sso.MnoGroup;
import com.maestrano.sso.MnoSession;
import com.maestrano.sso.MnoUser;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <pre>
 * User: rfh
 * Date: 21-12-2015
 *
 * </pre>
 */
@Singleton
public class NextAuthSAMLServlet extends HttpServlet {

    private static final String SAML_RESPONSE_PARAM = "SAMLResponse";

    public NextAuthSAMLServlet() {

    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            AuthRequest authReq = new AuthRequest(Maestrano.getDefault(), request);
            String ssoUrl = authReq.getRedirectUrl();
            response.sendRedirect(ssoUrl);
        } catch (final Exception e) {
            ServletOutputStream out = response.getOutputStream();
            e.printStackTrace();
            out.write(e.getMessage().getBytes());
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response authResp = null;
        try {
            authResp = new Response();
            authResp.loadXmlFromBase64(req.getParameter(SAML_RESPONSE_PARAM));

            if (authResp.isValid()) {

                // Build maestrano user and group objects
                MnoUser mnoUser = new MnoUser(authResp);
                MnoGroup mnoGroup = new MnoGroup(authResp);

                // No database model in this project. We just keep the
                // relevant details in session
                HttpSession sess = req.getSession();
                sess.setAttribute("loggedIn", true);
                sess.setAttribute("name", mnoUser.getFirstName());
                sess.setAttribute("surname", mnoUser.getLastName());
                sess.setAttribute("groupName", mnoGroup.getName());
                sess.setAttribute("groupId", mnoGroup.getUid());

                // Set Maestrano session (used for Single Logout)
                MnoSession mnoSession = new MnoSession(req.getSession(), mnoUser);
                mnoSession.save();

                // Redirect to you application home page
                req.getRequestDispatcher("/").forward(req, resp);

            } else {
                ServletOutputStream out = resp.getOutputStream();
                out.write("SAML Response is invalid".getBytes());
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            ServletOutputStream out = resp.getOutputStream();
            e.printStackTrace();
            out.write(e.getMessage().getBytes());
            out.flush();
            out.close();
        }
    }
}