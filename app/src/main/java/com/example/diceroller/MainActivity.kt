package com.example.diceroller

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diceroller.ui.DiceRollerTheme
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val DICE_STATE = "diceState"
    private val SHARED_PREFS = "sharedPrefs"

    private lateinit var imageResId: MutableState<Int>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        imageResId = mutableStateOf(sharedPreferences.getInt(DICE_STATE, R.drawable.dice_1)) // Or use R.drawable.empty_dice

        setContent {
            DiceRollerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    HomeScreen()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putInt(DICE_STATE, imageResId.value)
        sharedPreferencesEditor.apply()
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        DiceRollerTheme {
            HomeScreen()
        }
    }

    @Composable
    private fun HomeScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Image(
                    painterResource(id = imageResId.value),
                    "",
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
                Button(modifier = Modifier.align(alignment = Alignment.CenterHorizontally).padding(top = 16.dp),
                    onClick = { rollDice(imageResId) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3D8F3A))) {
                    Text("ROLL", color = Color.White)
                }
            }
        }
    }

    private fun rollDice(imageResId: MutableState<Int>) {
        var randomInt = 0
        var drawableResource: Int
        thread(start = true) {
            for (i in 0..49) {
                randomInt = Random.nextInt(6) + 1
                drawableResource = when (randomInt) {
                    1 -> R.drawable.dice_1
                    2 -> R.drawable.dice_2
                    3 -> R.drawable.dice_3
                    4 -> R.drawable.dice_4
                    5 -> R.drawable.dice_5
                    else -> R.drawable.dice_6
                }
                runOnUiThread { imageResId.value = drawableResource }
                Thread.sleep(50)
            }
            runOnUiThread {
                Toast.makeText(this, "Roll: $randomInt", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
