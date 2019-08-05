package techcourse.myblog.config.argumentresolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthorizedUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        return user;
    }
}
