package com.example.github_search;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtil {

    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private static final String GITHUB_USER = "users";
    private static final String GITHUB_REPOSITORY = "repositories";
    private static final String GITHUB_SEARCH = "search";
    private static final String PARAM_QUERY = "q";
    // Private Constructor so that class can't be instantiated [Singleton]
    private NetworkUtil() {
    }
    /**
     * Builds the URL used to query GitHub
     *
     * @param query The keyword that will be queried for.
     * @return The URL to use to query the GitHub server.
     */
    public static URL buildRepoSearchUrl(String query) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(GITHUB_SEARCH)
                .appendPath(GITHUB_REPOSITORY)
                .appendQueryParameter(PARAM_QUERY, query)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /**
     * Builds the URL used to query GitHub
     *
     * @param query The keyword that will be queried for.
     * @return The URL to use to query the GitHub server.
     */
    public static URL buildUserSearchUrl(String query) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendPath(GITHUB_SEARCH)
                .appendPath(GITHUB_USER)
                .appendQueryParameter(PARAM_QUERY, query)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /**
     * This method return the complete result from HTTP Response
     *
     * @param url The URL to fetch the HTTP Response from
     * @return The contents of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = urlConnection.getInputStream();
            // Scanner scanner = new Scanner(System.in);
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Parse the Github JSON response to List of Repositories
     *
     * @param repoJson Github JSON Response from  network
     * @return List of GithubRepository parsed
     */
    public static List<GithubRepository> parseGithubRepos(String repoJson) {
        List<GithubRepository> repositories = new ArrayList<>();
        if (TextUtils.isEmpty(repoJson)) return repositories;
        try {
            JSONObject root = new JSONObject(repoJson);
            JSONArray reposArray = root.getJSONArray("items");
            for (int i = 0; i < reposArray.length(); i++) {
                JSONObject repository = reposArray.getJSONObject(i);
                Integer id = repository.getInt("id");
                String name = repository.getString("name");
                String description = repository.getString("description");
                repositories.add(new GithubRepository(id, name, description));
                String url = repository.getString("html_url");
                repositories.add(new GithubRepository(id, name, description, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repositories;
    }



}
