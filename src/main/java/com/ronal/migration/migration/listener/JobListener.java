package com.ronal.migration.migration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JobListener extends JobExecutionListenerSupport {
    private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);


    //para el conteo de los registros en la tabla
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //posterior a la ejecución
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            LOG.info("FINALIZO EL JOB!! Verifica los resultados: ");

            Date start = jobExecution.getStartTime();
            Date end = jobExecution.getEndTime();

            long diff = end.getTime() - start.getTime();
            long total = jdbcTemplate.queryForObject("SELECT count(1) FROM tb_persona", Long.class);
            LOG.info("TIEMPO DE EJECUCIÓN: {}", TimeUnit.SECONDS.convert(diff,TimeUnit.MILLISECONDS));
            LOG.info("Registros: {}",total);
        }
    }
}
