package com.example.merquriassessment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.merquriassessment.MainActivity.ContactsViewModel
import com.example.merquriassessment.model.Contacts
import com.example.merquriassessment.ui.theme.MerquriAssessmentTheme

class DetailsScreen : ComponentActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerquriAssessmentTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {}
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailsView(
        _navController: NavController,
        viewModel: ContactsViewModel,
        _contacts: Contacts
    ) {
        navController = _navController
        val context = LocalContext.current
        val contacts = _contacts

        Scaffold(
            topBar = {
                Surface(shadowElevation = 5.dp) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "",
                                fontWeight = FontWeight.Medium
                            )
                        }, navigationIcon = {
                            TextButton(
                                onClick = {
                                    navController.navigate("mainScreen") {
                                        popUpTo("mainScreen")
                                        launchSingleTop = true
                                    }
                                },
                            ) {
                                Text(
                                    text = "Cancel",
                                    fontSize = 22.sp,
                                    color = Color(0xFFFF8C00),
                                )
                            }
                        }, actions = {
                            TextButton(
                                onClick = {
                                    if (contacts.firstName == "" || contacts.lastName == "") {
                                        Toast.makeText(
                                            context,
                                            "firstName, lastName can't be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.updateContacts(contacts)
                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            ) {
                                Text(
                                    text = "Save",
                                    fontSize = 22.sp,
                                    color = Color(0xFFFF8C00)
                                )
                            }
                        }, colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                            actionIconContentColor = Color(0xFFFF8C00),
                            navigationIconContentColor = Color(0xFFFF8C00)
                        )
                    )
                }
            },
            content = { padding ->
                Column(
                    Modifier

                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(
                            state = rememberScrollState(),
                            enabled = true
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(140.dp)
                                .background(Color(0xFFFF8C00))
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Main Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFe9e9e9))
                                .padding(horizontal = 16.dp, vertical = 5.dp),
                        )

                        InfoField("First Name", "${contacts.firstName}", onTextChange = {
                            contacts.firstName = it
                        })

                        InfoField("Last Name", "${contacts.lastName}", onTextChange = {
                            contacts.lastName = it
                        })

                        Text(
                            text = "Sub Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0XFFe9e9e9))
                                .padding(horizontal = 16.dp, vertical = 5.dp),
                        )

                        InfoField("Email", "${contacts.email}", onTextChange = {
                            contacts.email = it
                        })

                        InfoField("Phone", "${contacts.phone}", onTextChange = {
                            contacts.phone = it
                        })
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InfoField(fieldName: String, fieldContent: String, onTextChange: (String) -> Unit) {
        var textState by remember { mutableStateOf(fieldContent) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
            ) {
                Text(
                    text = fieldName,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            OutlinedTextField(
                modifier = Modifier.weight(4f),
                value = textState,
                onValueChange = {
                    textState = it
                    onTextChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                maxLines = 1,
                textStyle = TextStyle(fontSize = 15.sp)
            )
        }

        Divider(
            color = Color.LightGray, thickness = 1.dp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
