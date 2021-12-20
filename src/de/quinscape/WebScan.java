package de.quinscape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScan {

    public void extractTitle(String webAddress) throws IOException {
        String htmlContent = extractHtmlContent(webAddress);
        printTitleToConsole(htmlContent);
    }

    private String extractHtmlContent(String webAddress) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = readURL(webAddress);
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    private BufferedReader readURL(String webAddress) throws IOException {
        InputStream inputStream = getUrl(webAddress).openStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private URL getUrl(String webAddress) throws MalformedURLException {
        return new URL(webAddress);
    }

    private void printTitleToConsole(String htmlContent) {
        Pattern pattern = Pattern.compile("<title>(.*)<\\/title>");
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
