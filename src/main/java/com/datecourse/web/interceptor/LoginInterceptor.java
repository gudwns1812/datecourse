package com.datecourse.web.interceptor;

import static com.datecourse.web.constrant.SessionConst.MEMBER_ID;
import static com.datecourse.web.support.error.ErrorType.UNAUTHORIZED_USER;

import com.datecourse.web.support.error.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.debug("Login Check Interceptor");
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        log.debug("requestURI={}", requestURI);
        if (session == null || session.getAttribute(MEMBER_ID) == null) {
            throw new CoreException(UNAUTHORIZED_USER, null);
        }

        return true;
    }
}
