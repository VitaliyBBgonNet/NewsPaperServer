package com.dunice.GoncharovVVAdvancedServer.Interceptor;

import com.dunice.GoncharovVVAdvancedServer.constants.StringConstants;
import com.dunice.GoncharovVVAdvancedServer.entity.LogEntity;
import com.dunice.GoncharovVVAdvancedServer.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final LogRepository logRepository;

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : StringConstants.ANY_USER;

        LogEntity auditLog = new LogEntity();
        auditLog.setUsername(username);
        auditLog.setUrl(request.getRequestURI());
        auditLog.setHttpMethod(request.getMethod());
        auditLog.setClientIp(request.getRemoteAddr());
        auditLog.setStatusCode(String.valueOf(response.getStatus()));
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setErrorMessage(response.getHeader(StringConstants.ERROR_HEAD));
        logRepository.save(auditLog);
    }
}