package com.example.merquriassessment

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.merquriassessment.model.Contacts
import com.example.merquriassessment.model.DataParser
import com.example.merquriassessment.ui.theme.MerquriAssessmentTheme
import com.example.merquriassessment.utils.JsonUtils
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var viewModel: ContactsViewModel

    data class UiState(
        val refresh: Int = 0,
    )

    class ContactsViewModel : ViewModel() {
        private var _contactsList = mutableListOf<Contacts>()
        val contactsList: List<Contacts> get() = _contactsList

        private val _uiState = mutableStateOf(UiState())
        val uiState: State<UiState> get() = _uiState

        fun initContactsList(context: Context) {
            val jsonStr = JsonUtils().file2Json(context, "data.json")
            _contactsList = DataParser().json2Contacts(jsonStr)

            val newUiState = uiState.value.copy(
                refresh = uiState.value.refresh + 1
            )
            _uiState.value = newUiState
        }

        fun updateContacts(_contacts: Contacts) {
            for (contacts in _contactsList) {
                if (contacts.id == _contacts.id) {
                    contacts.firstName = _contacts.firstName
                    contacts.lastName = _contacts.lastName
                    contacts.email = _contacts.email
                    contacts.phone = _contacts.phone
                    break
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerquriAssessmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }

    @Composable
    fun Main() {
        viewModel = viewModel()
        viewModel.initContactsList(LocalContext.current)

        navController = rememberNavController()
        val detailsScreen = DetailsScreen()

        NavHost(navController = navController, startDestination = "mainScreen") {
            composable("mainScreen") {
                ContactsListView(viewModel)
            }
            composable(
                "detailsScreen/{id}/{firstName}/{lastName}?email={email}&phone={phone}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                    navArgument("firstName") { type = NavType.StringType },
                    navArgument("lastName") { type = NavType.StringType },
                    navArgument("email") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("phone") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                )
            ) {
                val id = it.arguments?.getString("id")
                val firstName = it.arguments?.getString("firstName")
                val lastName = it.arguments?.getString("lastName")
                val email = it.arguments?.getString("email")
                val phone = it.arguments?.getString("phone")
                val contacts = Contacts(id, firstName, lastName, email, phone)
                detailsScreen.DetailsView(navController, viewModel, contacts)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ContactsListView(viewModel: ContactsViewModel) {
        val lazyContactsListState = rememberLazyListState()

        Scaffold(
            topBar = {
                Surface(shadowElevation = 5.dp) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Contacts",
                                fontWeight = FontWeight.Medium
                            )
                        }, navigationIcon = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = null
                                )
                            }
                        }, actions = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    modifier = Modifier.size(30.dp),
                                    contentDescription = null
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
                val context = LocalContext.current
                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = false),
                    onRefresh = { viewModel.initContactsList(context) })
                {
                    LazyColumn(
                        state = lazyContactsListState,
                        modifier = Modifier
                            .padding(padding)
                            .background(Color.White)
                    ) {
                        items(viewModel.contactsList) { item ->
                            ContactsItem(item)
                        }
                    }
                }
            }


        )
    }

    @Composable
    fun ContactsItem(contacts: Contacts) {
        Column(modifier = Modifier
            .clickable {
                navController.navigate("detailsScreen/${contacts.id}/${contacts.firstName}/${contacts.lastName}?email=${contacts.email}&phone=${contacts.phone}")
            }
            .fillMaxSize()) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .background(Color(0xFFFF8C00))
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column() {
                    Text(
                        text = "${contacts.firstName} ${contacts.lastName}"
                    )
                }
            }
            Divider(
                color = Color.LightGray, thickness = 1.dp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

