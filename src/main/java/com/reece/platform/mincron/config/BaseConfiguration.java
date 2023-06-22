package com.reece.platform.mincron.config;

import com.ibm.as400.access.*;
import com.reece.platform.mincron.utilities.JDBCConnectionPoolListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class BaseConfiguration {
    @Value("${username_program_call}")
    protected String username;

    @Value("${password_program_call}")
    protected String password;

    @Value("${data_source_url}")
    protected String url;

    @Value("${connection_pool_size}")
    protected Integer maxConnectionPoolSize;

    @Value("${mincron_host_program_call}")
    private String mincronUrl;

    @Value("${connection_pool_listener_enabled:false}")
    private Boolean connectionPoolListenerEnabled;

    @Value("${tracing.as400.enabled:false}")
    private Boolean isTracingEnabled;

    @Value("${tracing.as400.conversion:false}")
    private Boolean conversionTracingEnabled;

    @Value("${tracing.as400.datastream:false}")
    private Boolean datastreamTracingEnabled;

    @Value("${tracing.as400.diagnostic:false}")
    private Boolean diagnosticTracingEnabled;

    @Value("${tracing.as400.error:false}")
    private Boolean errorTracingEnabled;

    @Value("${tracing.as400.information:false}")
    private Boolean informationTracingEnabled;

    @Value("${tracing.as400.pcml:false}")
    private Boolean pcmlTracingEnabled;

    @Value("${tracing.as400.proxy:false}")
    private Boolean proxyTracingEnabled;

    @Value("${tracing.as400.warning:false}")
    private Boolean warningTracingEnabled;

    @Bean
    public AS400ConnectionPool as400ConnectionPool() {
        log.info("Initializing AS400ConnectionPool");

        if (isTracingEnabled) {
            Trace.setTraceConversionOn(conversionTracingEnabled);
            Trace.setTraceDatastreamOn(datastreamTracingEnabled);
            Trace.setTraceDiagnosticOn(diagnosticTracingEnabled);
            Trace.setTraceErrorOn(errorTracingEnabled);
            Trace.setTraceInformationOn(informationTracingEnabled);
            Trace.setTracePCMLOn(pcmlTracingEnabled);
            Trace.setTraceProxyOn(proxyTracingEnabled);
            Trace.setTraceWarningOn(warningTracingEnabled);
            Trace.setTraceOn(isTracingEnabled);
        }

        var pool = new AS400ConnectionPool();

        if (connectionPoolListenerEnabled) {
            var connectionPoolListener = new JDBCConnectionPoolListener("AS400ConnectionPool");
            pool.addConnectionPoolListener(connectionPoolListener);
        }

        pool.setMaxConnections(maxConnectionPoolSize);
        // MaxInactivity: Will be closed after 10 seconds of inactivity by the maintenance thread (runs every 5 minutes)
        pool.setMaxInactivity(10000);

        return pool;
    }

    @Bean
    public AS400JDBCConnectionPool as400JDBCConnectionPool() {
        log.info("Initializing AS400JDBCConnectionPool");

        AS400JDBCConnectionPoolDataSource datasource =
                new AS400JDBCConnectionPoolDataSource(url);
        datasource.setUser(username);
        datasource.setPassword(password);
        datasource.setTrace(isTracingEnabled);

        var pool =  new AS400JDBCConnectionPool(datasource);

        if (connectionPoolListenerEnabled) {
            var connectionPoolListener = new JDBCConnectionPoolListener("AS400JDBCConnectionPool");
            pool.addConnectionPoolListener(connectionPoolListener);
        }

        pool.setMaxConnections(maxConnectionPoolSize);
        // MaxInactivity: Will be closed after 10 seconds of inactivity by the maintenance thread (runs every 5 minutes)
        pool.setMaxInactivity(10000);

        return pool;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
