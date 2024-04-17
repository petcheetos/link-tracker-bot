package edu.java.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class LinkProcessor {
    private static final Pattern GITHUB = Pattern.compile("https://github.com/(.+)/(.+)");
    private static final Pattern STACKOVERFLOW = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public boolean isValid(URI link) {
        return GITHUB.matcher(link.toString()).matches() || STACKOVERFLOW.matcher(link.toString()).matches();
    }

    public boolean isGithubUrl(URI link) {
        return GITHUB.matcher(link.toString()).matches();
    }

    public boolean isStackoverflowUrl(URI link) {
        return STACKOVERFLOW.matcher(link.toString()).matches();
    }

    public List<String> getUserRepoName(URI link) {
        List<String> userRepoNames = new ArrayList<>();
        String str = link.getPath();
        String[] arr = str.split("/");
        userRepoNames.add(arr[1]);
        userRepoNames.add(arr[2]);
        return userRepoNames;
    }

    public String getQuestionId(URI link) {
        String[] arr = link.getPath().split("/");
        return arr[2];
    }
}
