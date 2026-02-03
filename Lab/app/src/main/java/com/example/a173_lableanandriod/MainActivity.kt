package com.example.a173_lableanandriod

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a173_lableanandriod.ui.theme._173_LabLeanAndriodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RBGCardView(
               onNextActivity = {
                    startActivity(Intent(this, ListActivity3::class.java))
            })



        }

    }
}
    @Composable
    fun RBGCardView (onNextActivity: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(32.dp)
        ) {
            //hp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(Color.White)

            ) {


            }
            //
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(color = Color.White)

            ) {
                Text(
                    text = "hp",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterStart)
                        .fillMaxWidth(fraction = 0.5F)
                        .background(color = Color.Red)
                        .padding(8.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 40.dp)
                    .clickable {
                        onNextActivity.invoke()
                    }
            )

            var str by remember { mutableStateOf(8) }
            var Agi by remember { mutableStateOf(10) }
            var Int by remember { mutableStateOf(15) }


            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        str = str + 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }


                    Text(text = "Str", fontSize = 20.sp)
                    Text(text = str.toString(), fontSize = 25.sp)
                    Button(onClick = {
                        str = str - 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_downward_alt_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Button(onClick = {
                        Agi = Agi + 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }

                    Text(text = "Agi", fontSize = 20.sp)
                    Text(text = Agi.toString(), fontSize = 25.sp,)
                    Button(onClick = {
                        Agi = Agi - 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_downward_alt_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        Int = Int + 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                    Text(text = "Int", fontSize = 20.sp)
                    Text(text = Int.toString(), fontSize = 25.sp)

                    Button(onClick = {
                        Int = Int - 1
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_arrow_downward_alt_24),
                            contentDescription = "up",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }
