package com.petconomy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Order(2)
public class SqlLoader implements CommandLineRunner {

    private final DataSource dataSource;

    @Autowired
    public SqlLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        Resource resource = new ClassPathResource("data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
    }
}
