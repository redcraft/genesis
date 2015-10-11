package net.redcraft.genesis.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.redcraft.genesis.SlackURL;
import net.redcraft.genesis.SlackURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by RED on 11/10/2015.
 */

@Component
public class AirTable {

    private static final Logger log = LoggerFactory.getLogger(AirTable.class);
    private static final String TABLE_URL = "https://api.airtable.com/v0/app6e52Oq9b9YqhOQ/everything";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SlackURLRepository urlRepository;

    @Value("${airtable.apikey}")
    private String airtableAPIKey;

    private HttpsURLConnection getConnection(URL url) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Bearer " + airtableAPIKey);
        con.setRequestProperty("Content-type", "application/json");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        return con;
    }

//    @PostConstruct
    public void sync() throws Exception {
        int limit = 100;
        String offset = "";
        Set<String> urls = new HashSet<>();

        do {
            HttpsURLConnection con = getConnection(new URL(String.format(TABLE_URL + "?limit=%d&offset=%s", limit, offset)));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            JsonNode node = mapper.readTree(response.toString());
            JsonNode jsonUrls = node.get("records");
            for(JsonNode row : jsonUrls) {
                urls.add(row.get("fields").get("Link").asText().replaceAll("/$", ""));
            }
            offset = node.get("offset") != null ? node.get("offset").asText() : "";
        } while(!offset.equals(""));
        log.debug("Number of records in table: {}", urls.size());

        urlRepository.findAll().stream().filter(slackURL -> !urls.contains(slackURL.getUrl())).
            forEach(this::addRecord);
    }

    public void addRecord(SlackURL slackURL) {

        try {
            HttpsURLConnection con = getConnection(new URL(TABLE_URL));
            con.setRequestMethod("POST");

            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
            ObjectNode request = jsonNodeFactory.objectNode();
            request.put("Title", Optional.ofNullable(slackURL.getTitle()).orElse(""))
                    .put("Link", slackURL.getUrl())
                    .put("Description", Optional.ofNullable(slackURL.getDescription()).orElse(""))
                    .put("Date", "2015-09-14T15:53:31.000Z")
                    .put("channel", slackURL.getReferences().get(0).getChannel());
            String jsonRequest = mapper.writeValueAsString(jsonNodeFactory.objectNode().set("fields", request));
            log.debug("Airtable payload: {}", jsonRequest);

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(jsonRequest);
            writer.flush();
            writer.close();
            wr.close();
            int responseCode = con.getResponseCode();
            log.debug("Airtable response code: {}", responseCode);
            if(responseCode != 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                log.error("Can't post record to AirTable: {}", response.toString());
            }

        } catch (Exception e) {
            log.error("Error posting to Airtable", e);
        }
    }
}
