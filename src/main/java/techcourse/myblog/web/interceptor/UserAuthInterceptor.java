package techcourse.myblog.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static techcourse.myblog.web.SessionManager.USER;

@Component
public class UserAuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<User> userSession = Optional.ofNullable((User) request.getSession().getAttribute(USER));
        if (!userSession.isPresent()) {
            response.sendRedirect("/login");
            return false;
        }

        int index = request.getRequestURI().lastIndexOf("/");
        long uriId = Long.parseLong((request.getRequestURI().substring(index + 1)));
        long sessionId = userSession.get().getId();
        if (uriId == sessionId) {
            return true;
        }
        response.sendRedirect("/logout");
        return false;
    }
}