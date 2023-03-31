package com.code7979.readerszone.presentation.screens.main_screen.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.code7979.readerszone.R
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.data.remote.domain.model.Book
import com.code7979.readerszone.presentation.components.LoadingIndicator
import com.code7979.readerszone.presentation.navigation.Screen
import com.code7979.readerszone.presentation.screens.main_screen.vm.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Library(
    mainNavController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val bookResource by viewModel.allBook.collectAsState(Resource.Loading())
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Library")
            })
        }
    ) { paddingValues ->
        when (bookResource) {
            is Resource.Success -> {
                var books = emptyList<Book>()
                if (bookResource.data != null) {
                    books = bookResource.data!!
                }
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(items = books, key = { it.id }) { book ->
                        OutlinedCard(
                            modifier = Modifier
                                .animateItemPlacement(
                                    tween(durationMillis = 300)
                                )
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = {
                                mainNavController.navigate(
                                    route = Screen.BookDetailsScreen.passId(
                                        book.id
                                    )
                                )
                            },
                        ) {
                            ListItem(
                                leadingContent = {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(book.image)
                                            .crossfade(true)
                                            .build(),
                                        placeholder = painterResource(R.drawable.ic_image),
                                        contentDescription = null,
                                    )
                                },
                                headlineText = { Text(text = book.title) },
                                supportingText = {
                                    Text(
                                        text = book.subtitle,
                                        color = Color.Gray
                                    )
                                }
                            )
                        }
                    }
                }
            }
            is Resource.Error -> {
                val errorMsg = bookResource.message
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (errorMsg != null) {
                        Text(text = errorMsg)
                    }
                    Button(onClick = {
                        viewModel.getAllBooks()
                    }) {
                        Text(text = "Reload")
                    }

                }
            }
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            is Resource.Empty -> {}
        }
    }
}