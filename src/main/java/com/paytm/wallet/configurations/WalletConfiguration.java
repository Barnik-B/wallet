package com.paytm.wallet.configurations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.wallet.filters.EntryFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WalletConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public FilterRegistrationBean entryFilterBean() {
      return filterRegistrationBean(new EntryFilter(), 0);
    }

    private  FilterRegistrationBean filterRegistrationBean(Filter filter, int order) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter, new ServletRegistrationBean[0]);
        registrationBean.setFilter(new EntryFilter());
        registrationBean.setAsyncSupported(true);
        registrationBean.setOrder(order);
        registrationBean.addUrlPatterns("/api/v1/wallet/balance", "/api/v1/wallet/add", "/api/v1/wallet/withdraw", "/api/v1/wallet/transfer");
        return registrationBean;
    }
}
