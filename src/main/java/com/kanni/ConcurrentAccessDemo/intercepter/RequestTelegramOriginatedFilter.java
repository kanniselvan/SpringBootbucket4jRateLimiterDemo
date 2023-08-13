package com.kanni.ConcurrentAccessDemo.intercepter;

import com.kanni.ConcurrentAccessDemo.service.IPRequestLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(1)
public class RequestTelegramOriginatedFilter extends OncePerRequestFilter {

    @Autowired
    IPRequestLimiter ipRequestLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {
            System.out.println(req.getRequestURL()+"    -  "+req.getRequestURI());
            if (ipRequestLimiter.validateRegisterIP(req.getRemoteAddr(), req.getMethod() + "_" + req.getRequestURI())) {
                filterChain.doFilter(request, response);
            }
        } catch (Exception ex) {
            System.out.println("EX"+ex.getMessage());
            response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
            response.getWriter().println(ex.getMessage());
        }

    }

}

