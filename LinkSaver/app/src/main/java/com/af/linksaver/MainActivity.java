package com.af.linksaver;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.af.linksaver.data.model.Article;
import com.af.linksaver.data.remote.FirebaseHelper;
import com.af.linksaver.presentation.ArticleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;
    private RecyclerView articlesRecyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        firebaseHelper = new FirebaseHelper();

        if (firebaseHelper.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        articlesRecyclerView = findViewById(R.id.articlesRecyclerView);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticleAdapter(articles, this);
        articlesRecyclerView.setAdapter(articleAdapter);

        loadArticles();

        FloatingActionButton fab = findViewById(R.id.fabAddArticle);
        fab.setOnClickListener(view -> showAddArticleDialog());
    }

    private void loadArticles() {
        String userId = firebaseHelper.getCurrentUser().getUid();
        firebaseHelper.getUserArticles(userId, task -> {
            if (task.isSuccessful()) {
                articles.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Article article = document.toObject(Article.class);
                    articles.add(article);
                }
                articleAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Erro ao carregar artigos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddArticleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Artigo");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setHint("Cole a URL do artigo");
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String url = input.getText().toString().trim();
            if (!url.isEmpty()) {
                saveArticle(url);
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveArticle(String url) {
        String userId = firebaseHelper.getCurrentUser().getUid();
        Article article = new Article("Artigo sem título", url, userId);
        article.setId(UUID.randomUUID().toString());

        firebaseHelper.addArticle(article, task -> {
            if (task.isSuccessful()) {
                loadArticles();
            } else {
                Toast.makeText(MainActivity.this, "Erro ao salvar artigo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}