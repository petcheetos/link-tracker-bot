package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContainerBaseTest extends IntegrationTest {

    @Test
    void test() {
        assertThat(true).isTrue();
    }
}
