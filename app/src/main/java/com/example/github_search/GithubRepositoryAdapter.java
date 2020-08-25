package com.example.github_search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.github_search.databinding.ItemRepositoryBinding;

import java.util.List;

public class GithubRepositoryAdapter extends RecyclerView.Adapter<GithubRepositoryAdapter.GithubRepositoryViewHolder> {

    private List<GithubRepository> repositories;
    private LayoutInflater layoutInflater;

    public GithubRepositoryAdapter(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }

    @NonNull
    @Override
    public GithubRepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemRepositoryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_repository, parent, false);
        return new GithubRepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubRepositoryViewHolder holder, int position) {
        holder.binding.setRepo(repositories.get(position));
        holder.binding.getRoot().setOnClickListener(v -> {
            // Context from which Adapter is being called or Activity
            Context context = holder.binding.getRoot().getContext();

            // Url to Open
            String url = repositories.get(position).getUrl();

            // Create and launch web intent
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(webIntent);
        });
    }

    @Override
    public int getItemCount() {
        return repositories == null ? 0 : repositories.size();
    }

    static class GithubRepositoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemRepositoryBinding binding;

        GithubRepositoryViewHolder(@NonNull ItemRepositoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


