package com.senac.game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senac.game.ui.theme.GameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp();
                }
            }
        }
    }
}

enum class State {
    INITIAL, FAILED, SUCCESS, NEWGAME
}

@Composable
fun MyApp(){
    var randomNumberState = remember {
        mutableStateOf(0)
    }

    var Try = remember {
        mutableStateOf(State.INITIAL)
    }

    if (Try.value == State.NEWGAME || randomNumberState.value == 0){
         randomNumberState.value = (1..100).random();
        Try.value = State.INITIAL
    }

    var aproximation = remember {
        mutableStateOf("")
    }

    when (Try.value) {
        State.SUCCESS -> SuccessTry(onNewGame = { Try.value = State.NEWGAME })
        State.FAILED  -> FailedTry(aproximacao = aproximation.value, onRetry = { Try.value = State.INITIAL });
        State.INITIAL ->
            Initial(onTry = {
                Try.value = if (Integer.parseInt(it).equals(randomNumberState.value)) State.SUCCESS else State.FAILED;
                if (randomNumberState.value < Integer.parseInt(it))
                    aproximation.value =  "menor"
                else
                    aproximation.value =  "maior"
            })
    }
}

@Composable
fun Initial(onTry: (String) -> Unit) {
    var tryNumberState = remember {
        mutableStateOf("")
    };

    Column(verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment= Alignment.CenterHorizontally) {

        Text(text = "Adivinhe o n??mero entre 1 e 100", style = MaterialTheme.typography.h5, modifier = Modifier.padding(all = 25.dp))
        OutlinedTextField(value = tryNumberState.value, onValueChange = { tryNumberState.value = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        OutlinedButton(onClick =  {
                onTry(tryNumberState.value);
        }) {
            Text(text = "Tentar!")
        }
    }
}

@Composable
fun FailedTry(aproximacao: String, onRetry: () -> Unit){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Voce Errou!", style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 25.dp, bottom = 5.dp))
        Text(text = "O n??mero gerado ??: ${aproximacao}", style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 25.dp, bottom = 25.dp))
        OutlinedButton(onClick =  {
            onRetry()
        }) {
            Text(text = "Tentar novamente")
        }
    }
}

@Composable
fun SuccessTry(onNewGame: () -> Unit){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Parab??ns!", style = MaterialTheme.typography.h4, modifier = Modifier.padding(start = 25.dp, bottom = 5.dp))
        Text(text = "Voce acertou!", style = MaterialTheme.typography.h4, modifier = Modifier.padding(start = 25.dp, bottom = 25.dp))
        OutlinedButton(onClick =  {
            onNewGame()
        }) {
            Text(text = "Novo Jogo")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameTheme {
        MyApp();
    }
}