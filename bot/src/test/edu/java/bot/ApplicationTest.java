package edu.java.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private Bot bot;

    @Test
    void contextLoads() {
        assertThat(bot).isNotNull();
    }
}
