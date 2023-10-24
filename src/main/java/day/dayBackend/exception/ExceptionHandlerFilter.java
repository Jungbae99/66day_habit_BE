package day.dayBackend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import day.dayBackend.dto.response.ErrorResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            // 400
            errorResponse(response, HttpStatus.BAD_REQUEST, e);
        } catch (NotAuthenticatedException e) {
            // 401
            errorResponse(response, HttpStatus.UNAUTHORIZED, e);
        } catch (AccessDeniedException e) {
            // 403
            errorResponse(response, HttpStatus.FORBIDDEN, e);
        } catch (Exception e) {
            // 500
            errorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }

    }

    private void errorResponse(HttpServletResponse response, HttpStatus httpStatus, Exception e) throws IOException {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(
                ErrorResponseDto.builder()
                        .status(httpStatus.toString())
                        .message(e.getMessage())
                        .build()));
    }
}
