package siit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class ITBase {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupData() {
        FileSystemResource cleanScript = new FileSystemResource("src/main/database/cleandata.sql");
        ClassPathResource dataScript = new ClassPathResource(getClass().getSimpleName() + ".sql", getClass());
        new ResourceDatabasePopulator(cleanScript, dataScript).execute(jdbcTemplate.getDataSource());
    }
}
