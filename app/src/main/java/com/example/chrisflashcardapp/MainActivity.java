package com.example.chrisflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView FlashcardQuestion = findViewById(R.id.Flashcard_Question);
        TextView Answer = findViewById(R.id.Answer);
        FlashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlashcardQuestion.setVisibility(view.INVISIBLE);
                Answer.setVisibility(view.VISIBLE);
            }
        });
    }
}