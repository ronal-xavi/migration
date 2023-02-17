package com.ronal.migration.migration;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BachDefaultConfiguration extends DefaultBatchConfigurer {

    /* ===== PARA QUE NO CREE Y CONSULTE TABLAS AUTOMÁTICAMENTE =====*/

    @Override
    public void setDataSource(DataSource dataSource) {

    }
}
