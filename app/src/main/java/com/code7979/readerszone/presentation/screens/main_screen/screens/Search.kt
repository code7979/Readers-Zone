package com.code7979.readerszone.presentation.screens.main_screen.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.code7979.readerszone.presentation.components.SearchBox
import com.code7979.readerszone.presentation.navigation.Screen
import com.code7979.readerszone.presentation.screens.main_screen.vm.SearchViewModel
import com.code7979.readerszone.theme.CustomColor2
import com.code7979.readerszone.theme.md_theme_dark_onPrimary
import com.code7979.readerszone.theme.md_theme_dark_primary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Search(
    viewModel: SearchViewModel = hiltViewModel(),
    mainNavController: NavController
) {
    val bookResource by viewModel.books.collectAsState()
    val text by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    var onFocus by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Search Book")
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            SearchBox(
                value = text,
                onValueChange = viewModel::onSearchTextChange,
                backgroundColor = CustomColor2,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.padding(all = 10.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "image",
                        tint = if (onFocus) md_theme_dark_onPrimary else md_theme_dark_primary
                    )
                },
                onFocusChanged = {
                    onFocus = it.isFocused
                }
            )
            when (isSearching) {
                true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
                false ->{
                    when (bookResource) {
                        is Resource.Success -> {
                            var books = emptyList<Book>()
                            if (bookResource.data != null) {
                                books = bookResource.data!!
                            }
                            LazyColumn {
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
//                        viewModel.getAllBook()
                                }) {
                                    Text(text = "Reload")
                                }

                            }
                        }
                        is Resource.Empty -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No Book found")
                            }
                        }
                        else -> {}
                    }
                }
            }

        }
    }
}