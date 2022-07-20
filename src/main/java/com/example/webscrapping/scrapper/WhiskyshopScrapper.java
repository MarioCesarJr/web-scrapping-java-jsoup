package com.example.webscrapping.scrapper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.webscrapping.model.Whisky;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WhiskyshopScrapper
 */
public class WhiskyshopScrapper {

    public static void main(String[] args) throws IOException {

        Whisky whisky = new Whisky();
        String nextPage = null;
        
        String url = "https://www.whiskyshop.com/single-malt-scotch-whisky";

        System.out.println(String.format("Connect to url: %s", url));

        while (url != null) {
            // Connect to url 
            Document doc = Jsoup.connect(url).get();

            Elements whiskyList = doc.getElementsByClass("product-item-info");

            for (Element el : whiskyList) {
                whisky = new Whisky();
                whisky.setName(el.getElementsByClass("product-item-link").text());
                whisky.setPrice(el.getElementsByClass("price").text().replace("from £", "").replace("£", "")); 
                whisky.setLink(el.getElementsByClass("product-item-link").attr("href"));
                    
                // System.out.println(whisky);
                converterToJson(whisky);
            }
                    
            nextPage = doc.getElementsByClass("action  next").attr("href");
            url = nextPage != "" ? nextPage : null;
        }
    }

    private static void converterToJson(Whisky whisky){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(whisky);
            System.out.println("JSON OBJECT: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}