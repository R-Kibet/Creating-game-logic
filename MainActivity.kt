package com.example.anew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anew.models.BoardSize
import com.example.anew.models.MemoryCard
import com.example.anew.models.MemoryGame
import com.example.anew.utils.Default_Icon
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {




    //lateinit - variables will not be created at the time of initialization || only happen on create
    private lateinit var memoryGame: MemoryGame
    private lateinit var mroot : ConstraintLayout
    private lateinit var rvBoard : RecyclerView
    private lateinit var  moves: TextView
    private lateinit var  pairs :TextView
    private lateinit var adapter: MemoryBoardAdapter

    //setting the game board side dynamically depending on   game option
    private var  boardSize: BoardSize = BoardSize.HARD

    companion object{
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //initialize
        rvBoard = findViewById(R.id.recView)
        moves = findViewById(R.id.moves)
        pairs = findViewById(R.id.pairs)
        mroot = findViewById(R.id.mroot)

        //make this property of a class so to be refrenced
        memoryGame = MemoryGame(boardSize)

        //recyclerview = contains layout manager and adapter

        //creating adapter
         adapter = MemoryBoardAdapter(this,boardSize , memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun cardClicked(position: Int) {
                  //  Log.i(TAG , "Card clicked $position")

                    //creating a game logic
                    updateGameWithFlip(position)
                }

            }
        
        ) //context and how many total elements in the grid


        rvBoard.adapter = adapter

        //for perfomance optimization
        rvBoard.setHasFixedSize(true)
        

       //creating layout manager
        //grid layout manager creates the grid effect - takes 2 parameters context , columns
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())//hard coded 2 columns but later shall change to dynamic


        //edge effect - overscoll mode= never


    }

    private fun updateGameWithFlip(position: Int) {

        //Error management
        if (memoryGame.haveWon()){
            //Alert user of an invalid move

           Snackbar.make(mroot,"you already won",Snackbar.LENGTH_LONG).show()
            return
        }

        if (memoryGame.isCardFaceUp(position)){
            //alert the user invalid move
                Snackbar.make(mroot,"Invalid move", Snackbar.LENGTH_LONG).show()
            return
        }



        //for this function created memory game and adapter parameters

      if(  memoryGame.flipCard(position)){
          Log.i(TAG, "Match ${memoryGame.pairsFound}")
      }

        //changes that occur in position of the card
        adapter.notifyDataSetChanged()

    }
}