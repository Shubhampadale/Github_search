package com.example.github_search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.github_search.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.btnsearch.setOnClickListener(v -> {
            String searchTerm = binding.edtsearch.getText().toString();
            if (TextUtils.isEmpty(searchTerm)) return;
            URL searchUrl = NetworkUtil.buildRepoSearchUrl(searchTerm);
            new GithubQueryTask().execute(searchUrl);
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtil.getResponseFromHttp(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }
        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                parseAndDisplayRepos(githubSearchResults);
            }
        }
    }

    /**
     * Parse the JSON Response from network and display Output
     *
     * @param json Github JSON Response form the network
     */
    private void parseAndDisplayRepos(String json) {
        // Parse List
        List<GithubRepository> repositories = NetworkUtil.parseGithubRepos(json);
        StringBuilder sb = new StringBuilder();
        for (GithubRepository repository : repositories) {
            sb.append("Id: ").append(repository.getId())
                    .append("\n")
                    .append("Name: ").append(repository.getName())
                    .append("\n\n");
        }


        // Create List Adapter
        GithubRepositoryAdapter adapter = new GithubRepositoryAdapter(repositories);

        // Set Adapter and layout type to RecyclerView
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
    }
}
