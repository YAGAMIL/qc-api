package com.quantumtime.qc.common.config;

import com.quantumtime.qc.common.filter.GlobalRequestContextFilter;
import com.quantumtime.qc.common.filter.HttpTraceLogFilter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class GlobalRequestContextConfiguration {
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletTraceFilterConfiguration {
        @Bean
        public GlobalRequestContextFilter globalRequestContext(MeterRegistry registry) {
            return new GlobalRequestContextFilter(registry);
        }

    }

}
