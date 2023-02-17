package com.ronal.migration.migration;

import com.ronal.migration.migration.model.Persona;
import com.ronal.migration.migration.listener.JobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

//@Configuration
//@EnableBatchProcessing
public class BatchConfiguration {

    /* ===== Inyectamos como dependencia la clase JobBuilderFactory =====*/
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    /* ===== Inyectamos como dependencia la clase StepBuilderFactory =====*/
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Persona> reader(){
        return new FlatFileItemReaderBuilder<Persona>()
                .name("personaItemReader")
                .resource(new ClassPathResource("lib100000.xlsx"))
                .delimited()
                .names(new String[]{"nombre","apellido","direccion","telefono"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>(){{
                    setTargetType(Persona.class);
                }})
                //.linesToSkip(1) //Para saltar una fila, esto se hace para encabezados
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Persona> writer (DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Persona>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Persona>())
                .sql("INSERT INTO PERSONA(nombre, apellido, direccion, telefono) VALUES (:nombre, :apellido, :direccion, :telefono)")
                .dataSource(dataSource)
                .build();
    }

    //Nos indica cuando el proceso del barch arranco y cuando finalizo
    @Bean
    public Job importPersonaJob(JobListener listener, Step step1){
        return jobBuilderFactory.get("importPersonaJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    public Step step1 (JdbcBatchItemWriter<Persona> writer){
        return stepBuilderFactory.get("step1")
                .<Persona,Persona> chunk(1000)
                .reader(reader())
                .writer(writer)
                .build();
    }





}
