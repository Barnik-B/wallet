package com.paytm.wallet.filters;

import com.google.gson.Gson;
import com.paytm.wallet.model.response.ApiResponse;
import com.paytm.wallet.model.response.Meta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.paytm.wallet.constants.CommonConstants.USER_ID_EMPTY_ERROR;

@Slf4j
public class EntryFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(StringUtils.isBlank(request.getHeader("userId"))) {
            ApiResponse apiResponse = new ApiResponse();
            Meta meta = new Meta();
            meta.setCode("001");
            meta.setMessage(USER_ID_EMPTY_ERROR);
            apiResponse.setMeta(meta);
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            String finalResponse = new Gson().toJson(apiResponse);
            out.print(finalResponse);
            out.flush();
            log.error("[EntryFilter.doFilter] Error message :: {}", USER_ID_EMPTY_ERROR);
            log.error("[EntryFilter.doFilter] HTTPStatus :: {}", HttpStatus.FORBIDDEN.name());
            return;
        }
        MDC.put("userId", request.getHeader("userId"));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}