package eu.rebase.recipe.controller;

import eu.rebase.recipe.model.Identity;
import eu.rebase.recipe.exception.UserIdNotSetException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * To parse and read identity from headers of request
 */
public class IdentityArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String X_USERID = "X-USERID";
    public static final String MISSING_HEADER_X_USERID = "Missing header X-USERID";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Identity.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory) {
        return Optional.ofNullable(nativeWebRequest.getHeader(X_USERID))
                .filter(id -> !id.isEmpty())
                .map(id -> Identity.builder()
                        .id(id)
                        .build())
                .orElseThrow(() -> new UserIdNotSetException(MISSING_HEADER_X_USERID));
    }
}
