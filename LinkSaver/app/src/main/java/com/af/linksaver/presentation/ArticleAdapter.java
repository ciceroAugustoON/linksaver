package com.af.linksaver.presentation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.af.linksaver.R;
import com.af.linksaver.data.model.Article;
import com.af.linksaver.data.remote.FirebaseHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private Context context;

    public ArticleAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.titleTextView.setText(article.getTitle());
        holder.excerptTextView.setText(article.getExcerpt());

        // Carregar imagem se existir
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(article.getImageUrl())
                    .into(holder.articleImageView);
        }

        // Configurar favorito
        holder.favoriteButton.setImageResource(
                article.isFavorite() ? R.drawable.star : R.drawable.star_border);

        // Configurar cliques
        holder.itemView.setOnClickListener(v -> {
            // Abrir artigo no navegador ou WebView
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
            context.startActivity(browserIntent);
        });

        holder.favoriteButton.setOnClickListener(v -> {
            boolean newFavoriteState = !article.isFavorite();
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.toggleFavorite(article.getId(), newFavoriteState, task -> {
                if (task.isSuccessful()) {
                    article.setFavorite(newFavoriteState);
                    notifyItemChanged(position);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView excerptTextView;
        ImageView articleImageView;
        ImageButton favoriteButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            excerptTextView = itemView.findViewById(R.id.excerptTextView);
            articleImageView = itemView.findViewById(R.id.articleImageView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
