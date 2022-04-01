package vttp2022.ssf.assessment.videosearch.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.assessment.videosearch.models.Game;

@Service
public class SearchService {


    // cannot set the environment var
    //@Value("${rawg.api.key}")
    //private String apiKey;
    private String apiKey="3a568030761741a0b81ee01e82cc3d02";

    public List<Game> search(String searchString, Integer count) {

        List<Game> list = new ArrayList<Game>();

        
        final String URL = "https://api.rawg.io/api/games";
        String finalURL = UriComponentsBuilder
                    .fromUriString(URL)
                    .queryParam("key", apiKey)
                    .queryParam("search", searchString)
                    .queryParam("page_size", count)
                    .toUriString();

        RequestEntity<Void> req = RequestEntity
        .get(finalURL)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;

        resp = template.exchange(req, String.class);


        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            //System.out.println(">>>>> service req readobject: " + o);

            JsonArray results = o.getJsonArray("results");
            
            // Check
            //System.out.println(">>>>> service start arraylist: " + results);

            for (int i=0; i <results.size(); i++) {
                JsonObject temp = results.getJsonObject(i);
                Game game = new Game();

                game.setName(temp.getString("name"));
                game.setBackgroundImage(temp.getString("background_image"));
                game.setRating((float) temp.getInt("rating"));

                list.add(game);
            }
            

        } catch (Exception ex) {
            System.err.printf(">>>> service Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println(">>>>> service list: " + list);
        return list;
        
    }



}
