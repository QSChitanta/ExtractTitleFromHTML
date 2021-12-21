package de.quinscape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScan {

    private static final Set<String> visitedLinkCache = new HashSet<>();

    public void scanWebAddress(String webAddress) throws IOException {
        webAddress = normalizeUrl(webAddress);
        if (visitedLinkCache.contains(webAddress)){
            return;
        }
        visitedLinkCache.add(webAddress);
        String htmlContent = extractHtmlContent(webAddress);
        printTitleToConsole(htmlContent);
        printAllLinks(htmlContent);
    }

    private String extractHtmlContent(String webAddress) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = readUrl(webAddress);
         while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    private BufferedReader readUrl(String webAddress) throws IOException {
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
            System.out.println("Title: " + matcher.group(1));
        }
    }

    private void printAllLinks(String htmlContent) throws IOException {
        Pattern pattern = Pattern.compile("<a href=\"(.*?)\"");
        Matcher matcher = pattern.matcher(htmlContent);
        int counter = 0;
        while (matcher.find()) {
            String link = matcher.group(1);
            if (isValidLink(link)){
                System.out.println("Link " + counter + ": " + link);
                counter++;
                diveIntoExternalLink(link);
            }
        }
        System.out.println("Amount of Links: " + (counter-1));
    }

    private boolean isValidLink(String link) {
        return !link.startsWith("#");
    }

    private void diveIntoExternalLink(String link) throws IOException {
        if (link.startsWith("https://") || link.startsWith("http://")){
            diveIntoLinkAndScanAgain(link);
        }
    }

    private void diveIntoLinkAndScanAgain(String diveLink) throws IOException {
        WebScan diveDeeper = new WebScan();
        diveDeeper.scanWebAddress(diveLink);
    }

    private String normalizeUrl(String link){
        if (!link.startsWith("https://") && !link.startsWith("http://")){
            return "https://" + link;
        }
        return link;
    }
}
