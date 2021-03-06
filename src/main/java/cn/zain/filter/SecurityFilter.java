package cn.zain.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SecurityFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURL().toString();

        if (!url.endsWith(".do") || url.endsWith("/sysuser/forward/login.do")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = req.getSession(true);
        if (session.getAttribute("sysUser") != null) {
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute("error", "非法请求");
        logger.warn("非法请求 : " + request.getRemoteAddr() + url);
//        resp.sendRedirect(req.getContextPath() + "/sysuser/forward/login.do");
        request.setAttribute("path",req.getContextPath());
        request.getRequestDispatcher("/sysuser/forward/login.do").forward(request,response);
        return;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
