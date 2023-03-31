package com.code7979.readerszone.presentation.screens.main_screen.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.code7979.readerszone.presentation.components.LoadingIndicator
import com.code7979.readerszone.presentation.navigation.Screen
import com.code7979.readerszone.presentation.screens.main_screen.vm.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Books(
    viewModel: BooksViewModel = hiltViewModel(),
    mainNavController: NavController
) {
    val books by viewModel.allBooks.collectAsState(initial = Resource.Loading())
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "My Books")
            })
        }
    ) { paddingValues ->
        when (books) {
            is Resource.Success -> {
                books.data?.let { books ->
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
                                            book.book_id
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
                                    },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            viewModel.deleteBook(bookId = book.book_id)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete this book"
                                            )
                                        }
                                    }
                                )
                            }
                        }
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
            is Resource.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = LocalContext.current.getString(R.string.empty_list))
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Something went wrong \n Plz Restart App")
                }
            }
        }

    }
}