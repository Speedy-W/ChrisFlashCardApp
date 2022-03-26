package com.example.chrisflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView FlashcardQuestion;
    TextView Answer;

    FlashcardDatabase flashcardDatabase;
    List< Flashcard> allFlashcards;
    int cardIndex = 0;

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
        Answer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlashcardQuestion.setVisibility(view.VISIBLE);
                Answer.setVisibility(view.INVISIBLE);
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

        flashcardDatabase = new FlashcardDatabase( getApplicationContext() );
        allFlashcards =  flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size() > 0) {
            Flashcard first = allFlashcards.get( 0 );
            FlashcardQuestion.setText( first.getQuestion() );
            Answer.setText( first.getAnswer() );
        }

        findViewById( R.id.Next).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFlashcards == null || allFlashcards.size() ==0){
                    return;
                }

                cardIndex +=1;

                if(cardIndex >=allFlashcards.size()){
                    Snackbar.make(view,
                            "You've  reached the end of the card! Going back to the start",
                            Snackbar.LENGTH_SHORT).show();
                    cardIndex = 0;

                }

                Flashcard currentCard =   allFlashcards.get( cardIndex );
                FlashcardQuestion.setText( currentCard.getQuestion() );
                Answer.setText( currentCard.getAnswer() );
            }
        } );
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

                Flashcard flashcard = new Flashcard( string1,string2 );
                flashcardDatabase.insertCard( flashcard );

                allFlashcards = flashcardDatabase.getAllCards();

            }
        }
    }
}