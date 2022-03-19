package com.example.chrisflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView FlashcardQuestion;
    TextView Answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlashcardQuestion = findViewById(R.id.Flashcard_Question);
        Answer = findViewById(R.id.Answer);
        FlashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlashcardQuestion.setVisibility(view.INVISIBLE);
                Answer.setVisibility(view.VISIBLE);
            }
        } );
        ImageView Plus = findViewById(R.id.Plus_Sign);
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult( intent,100 );
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 100) {
            if (data != null) {
                String string1 = data.getExtras().getString( "Question" );
                String string2 = data.getExtras().getString( "Answer" );
                FlashcardQuestion.setText( string1 );
                Answer.setText( string2 );
            }
        }
    }
}