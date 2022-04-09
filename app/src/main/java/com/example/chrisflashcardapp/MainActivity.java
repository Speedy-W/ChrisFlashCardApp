package com.example.chrisflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView FlashcardQuestion;
    TextView Answer;

    FlashcardDatabase flashcardDatabase;
    Flashcard currentCard;
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
                int cx = Answer.getWidth() / 2;
                int cy = Answer.getHeight() / 2;

                float finalRadius = (float) Math.hypot(cx, cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(Answer, cx, cy, 0f, finalRadius);

                FlashcardQuestion.setVisibility(view.INVISIBLE);
                Answer.setVisibility(view.VISIBLE);
                anim.setDuration(1000);
                anim.start();
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

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
                            "You've reached the end of the card! Going back to the start",
                            Snackbar.LENGTH_SHORT).show();
                    cardIndex = 0;

                }
                currentCard = allFlashcards.get( cardIndex );
                FlashcardQuestion.setText( currentCard.getQuestion() );
                Answer.setText( currentCard.getAnswer() );

                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                findViewById(R.id.Flashcard_Question).startAnimation(leftOutAnim);
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.Flashcard_Question).startAnimation(rightInAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

            }
        } );
        findViewById( R.id.Delete ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.Flashcard_Question)).getText().toString());

                if(cardIndex > 1){
                    cardIndex--;
                    allFlashcards = flashcardDatabase.getAllCards();
                    currentCard = allFlashcards.get( cardIndex );
                    FlashcardQuestion.setText( currentCard.getQuestion() );
                    Answer.setText( currentCard.getAnswer() );
                }
                else{
                    Snackbar.make(view,
                            "You have no cards left!",
                            Snackbar.LENGTH_SHORT).show();
                    FlashcardQuestion.setText( "Add a card!" );
                    Answer.setText( "Please add a card!" );
                }

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