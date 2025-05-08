import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AzureOCR {
    private static final String ENDPOINT = "https://meuocr.cognitiveservices.azure.com/";
    private static final String API_KEY = "gsbgajcVu4hRbYGBX9BIAYV6b4uPo8yKfuv5VhsmOM9wXZgfbp8nJQQJ99BEACYeBjFXJ3w3AAAFACOG2Ka9";
    private static final String IMAGE_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsDyFPHESytseKT5iHG0lg05OUpAZ88v4kkg&s";

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(ENDPOINT + "/vision/v3.2/ocr").newBuilder()
                .addQueryParameter("language", "unk") 
                .addQueryParameter("detectOrientation", "true")
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String json = "{ \"url\": \"" + IMAGE_URL + "\" }";
        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonData = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonData);

            System.out.println("Texto detectado na imagem:");
            for (JsonNode region : root.get("regions")) {
                for (JsonNode line : region.get("lines")) {
                    StringBuilder lineText = new StringBuilder();
                    for (JsonNode word : line.get("words")) {
                        lineText.append(word.get("text").asText()).append(" ");
                    }
                    System.out.println(lineText.toString().trim());
                }
            }
        }
    }
}
