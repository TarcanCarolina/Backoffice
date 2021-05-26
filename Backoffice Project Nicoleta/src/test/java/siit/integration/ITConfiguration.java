package siit.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@Import(TestDatabaseAutoConfiguration.class)
public class ITConfiguration {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    void setupSchema() {
        FileSystemResource schemaScript = new FileSystemResource("src/main/database/schema.sql");
        new ResourceDatabasePopulator(schemaScript).execute(dataSource);
    }

}
