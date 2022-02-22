package wikipediaAPI;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class wikiAPIManager {

    //get attraction description from WikipediaAPI by url
    public static String getAttractionDescriptionFromWikiWithUrl(String url) throws IOException {

        String description=null;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines = reader.lines().collect(Collectors.joining());

        if(!lines.contains("extract"))
        {
            reader.close();
            return null;
        }

        String descriptionHtml = lines.split("\"extract\":\"")[1];
        String descriptionText = Jsoup.parse(descriptionHtml).wholeText();
        descriptionText = descriptionText.replaceAll("\\\\n", "\n");

        String[] fourFirstSentences = descriptionText.split("[.]", 5);
        fourFirstSentences[0] = fourFirstSentences[0].replaceAll("\n", "");
        reader.close();

        if(fourFirstSentences.length < 4){
            description = null;

        }

        else {
            description = (fourFirstSentences[0].trim() + ".\n" + fourFirstSentences[1].trim() + ".\n" + fourFirstSentences[2].trim() + ".\n" + fourFirstSentences[3].trim() + ".\n");
        }
        return description;
    }

    //get attraction description from WikipediaAPI by attraction name
    public static String getAttractionDescriptionFromWiki(String attractionName) throws IOException {
        String descriptionUrl=null;
        String newAttractionName = attractionName.replace(" ", "_");

        String url = "https://en.wikipedia.org/w/api.php?action=query&titles="+newAttractionName+"&prop=extracts&format=json";
        descriptionUrl = getAttractionDescriptionFromWikiWithUrl(url);

        if(descriptionUrl==null) {
            url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + newAttractionName + ",_London&prop=extracts&format=json";
            descriptionUrl = getAttractionDescriptionFromWikiWithUrl(url);
        }
        if(descriptionUrl==null) {
            newAttractionName = newAttractionName.replace("The","");
            url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + newAttractionName +"&prop=extracts&format=json";
            descriptionUrl = getAttractionDescriptionFromWikiWithUrl(url);
        }
        return descriptionUrl;
    }

    //get attraction image from WikipediaAPI by attraction name
    public static String getAttractionImageFromWiki(String attractionName) throws IOException {
        String imageUrl=null;
        String newAttractionName = attractionName.replace(" ", "_");

        String url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + newAttractionName +"&prop=pageimages&format=json&pithumbsize=300";
        imageUrl = getAttractionImageFromWikiWithUrl(url);

        if(imageUrl==null) {
            url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + newAttractionName + ",_London&prop=pageimages&format=json&pithumbsize=300";
            imageUrl = getAttractionImageFromWikiWithUrl(url);
        }
        if(imageUrl==null) {
            newAttractionName = newAttractionName.replace("The","");
            url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + newAttractionName +"&prop=pageimages&format=json&pithumbsize=300";
            imageUrl = getAttractionImageFromWikiWithUrl(url);
        }
        return imageUrl;
    }

    //get attraction image from WikipediaAPI by url
    public static String getAttractionImageFromWikiWithUrl(String url) throws IOException {

        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines = reader.lines().collect(Collectors.joining());

        if(!lines.contains("source"))
        {
            reader.close();
            return null;
        }
        String imageSource = lines.split("\"source\":\"")[1];
        imageSource = imageSource.replaceAll("\"", " ");
        imageSource = imageSource.split("width")[0].replace(",", "").trim();

        reader.close();

        return(imageSource);

    }
}
