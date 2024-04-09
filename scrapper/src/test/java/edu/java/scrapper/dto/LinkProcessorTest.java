package edu.java.scrapper.dto;

import edu.java.dto.LinkProcessor;
import java.net.URI;
import java.util.List;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkProcessorTest {

    private final LinkProcessor linkProcessor = new LinkProcessor();

    @Test
    public void testIsValid() {
        assertTrue(linkProcessor.isValid(URI.create("https://github.com/petcheetos/backend-java-course")));
        assertTrue(linkProcessor.isValid(URI.create("https://stackoverflow.com/questions/35070455")));
        assertFalse(linkProcessor.isValid(URI.create("1")));
        assertFalse(linkProcessor.isValid(URI.create("https://www.google.ru")));
    }

    @Test
    public void testIsGithubUrl() {
        assertTrue(linkProcessor.isGithubUrl(URI.create("https://github.com/petcheetos/backend-java-course")));
        assertFalse(linkProcessor.isGithubUrl(URI.create("https://stackoverflow.com/questions/35070455")));
    }

    @Test
    public void testIsStackoverflowUrl() {
        assertFalse(linkProcessor.isStackoverflowUrl(URI.create("https://github.com/petcheetos/backend-java-course")));
        assertTrue(linkProcessor.isStackoverflowUrl(URI.create("https://stackoverflow.com/questions/35070455")));
    }

    @Test
    public void testGetUserRepoName() {
        assertEquals(
            linkProcessor.getUserRepoName(URI.create("https://github.com/petcheetos/backend-java-course")),
            List.of("petcheetos", "backend-java-course")
        );
    }

    @Test
    public void testGetQuestionId() {
        assertEquals(
            linkProcessor.getQuestionId(URI.create("https://stackoverflow.com/questions/35070455")),
            "35070455"
        );
    }
}
