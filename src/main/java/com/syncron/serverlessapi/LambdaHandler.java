package com.syncron.serverlessapi;

import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author chetanmelkani
 *
 */
public class LambdaHandler {

    private static final Logger logger = Logger.getLogger(LambdaHandler.class);
    private final MovieDAO movieDAO = new MovieDAO();
    private static JsonParser jsonParser = new JsonParser();

    public void handle(final InputStream inputStream, final OutputStream outputStream) {
        try {
            JsonObject jsonObject = getJSONObject(inputStream);
            String response = requestRouter(jsonObject);

            writeResponse(outputStream, response);

        } catch (final Exception e) {
            logger.fatal("unchecked internal error", e);
        }
    }

    private Gson getGson() {
        return new Gson();
    }

    private JsonObject getJSONObject(final InputStream inputStream) throws IOException {
        final String input = IOUtils.toString(inputStream);
        logger.info("input the lambda: " + input);

        return parseAsJsonObject(input);
    }

    private void writeResponse(final OutputStream outputStream, final String input) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(input);
        writer.close();
    }

    private String requestRouter(JsonObject jsonObject) {
        String action = jsonObject.get("action").getAsString();
        logger.info("lambda action:" + action);
        String response;

        switch (action) {
        case "CreateMovie":
            Movie movie = getGson().fromJson(jsonObject.getAsJsonObject("body"), Movie.class);
            logger.info("in create movie : " + movie);
            response = serialize(movieDAO.create(movie), Movie.class);
            break;
        case "GetMovie":
            logger.info("in get movie");
            Movie m = getMovieModel(jsonObject);
            response = serialize(movieDAO.get(m), Movie.class);
            break;
        case "DeleteMovie":
            logger.info("in delete movie");
            Movie m1 = getMovieModel(jsonObject);
            response = serialize(movieDAO.delete(m1), Movie.class);
            break;
        case "ListMovies":
            logger.info("in list movies");
            List<Movie> movies = movieDAO.list();
            response = serialize(movies, movies.getClass());
            break;
        default:
            logger.info("No action is defined");
            throw new RuntimeException("no action defined");
        }

        return response;
    }

    private Movie getMovieModel(JsonObject jsonObject) {
        String id = jsonObject.getAsJsonObject("params").getAsJsonObject("path").get("id").getAsString();
        Movie m = new Movie();
        m.setId(id);
        return m;
    }

    private String serialize(Object payload, Class<?> type) {
        return getGson().toJson(payload, type);
    }

    private JsonObject parseAsJsonObject(final String str) {
        try {
            return jsonParser.parse(str).getAsJsonObject();
        } catch (final JsonSyntaxException e) {
            logger.error("INVALID_JSON : input parsing failed", e);
            throw new RuntimeException("input parsing failed");
        }
    }

}
