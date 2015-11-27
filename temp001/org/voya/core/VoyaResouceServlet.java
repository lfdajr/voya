package org.voya.core;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class VoyaResouceServlet extends HttpServlet {

    protected String getRelativePath(HttpServletRequest request) {
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("PathInfo: " + request.getPathInfo());
        System.out.println("SerlvletPath: " + request.getServletPath());
        
        return request.getServletPath() + request.getPathInfo();
    }
}
