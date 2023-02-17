package com.ronal.migration.migration.antiguita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import com.ronal.migration.migration.listener.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class MigrationAntiguita implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public static void main(String[] args) {
    SpringApplication.run(MigrationAntiguita.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    LOG.info("INICIANDO EJECUCIÓN ...");
    long start = System.currentTimeMillis();

    File file = new File("ronal.txt");

    BufferedReader reader = new BufferedReader(new FileReader(file));

    String row = null;

    while ((row = reader.readLine()) != null) {
      Object[] data = row.split(",");

      jdbcTemplate.update("INSERT INTO tb_persona (nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?)", data);

    }

    long total = jdbcTemplate.queryForObject("SELECT count(1) FROM tb_persona", Long.class);
    long end = System.currentTimeMillis();
    long diff = end - start;

    reader.close();

    LOG.info("TIEMPO DE EJECUCIÓN: {}", TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
    LOG.info("Registros: {}", total);


  }
}
