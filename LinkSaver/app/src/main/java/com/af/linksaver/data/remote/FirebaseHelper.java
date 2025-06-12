package com.af.linksaver.data.remote;

import com.af.linksaver.data.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseHelper {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addArticle(Article article, final OnCompleteListener<Void> listener) {
        db.collection("articles")
                .document(article.getId())
                .set(article)
                .addOnCompleteListener(listener);
    }

    public void getUserArticles(String userId, final OnCompleteListener<QuerySnapshot> listener) {
        db.collection("articles")
                .whereEqualTo("userId", userId)
                .whereEqualTo("isArchived", false)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(listener);
    }

    public void archiveArticle(String articleId, final OnCompleteListener<Void> listener) {
        db.collection("articles")
                .document(articleId)
                .update("isArchived", true)
                .addOnCompleteListener(listener);
    }

    public void toggleFavorite(String articleId, boolean isFavorite, final OnCompleteListener<Void> listener) {
        db.collection("articles")
                .document(articleId)
                .update("isFavorite", isFavorite)
                .addOnCompleteListener(listener);
    }

    public void signIn(String email, String password, final OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void signUp(String email, String password, final OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void signOut() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}
