package com.code7979.readerszone.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.work.WorkInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.code7979.readerszone.R
import com.code7979.readerszone.data.Resource
import com.code7979.readerszone.presentation.components.LoadingIndicator
import com.code7979.readerszone.presentation.model.BookDetail
import com.code7979.readerszone.presentation.model.BookState
import com.code7979.readerszone.presentation.navigation.Screen
import com.code7979.readerszone.theme.appTypography
import com.code7979.readerszone.util.replaceFS
import com.code7979.readerszone.vm.BookDetailsViewModel
import com.code7979.readerszone.worker.WorkerConstant

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    navController: NavHostController,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    val bookDetails by viewModel.bookDetails.collectAsState()
    val bookState by viewModel.bookState.collectAsState()
    val workInofs = viewModel.workInfos.observeAsState().value
    val downloadInfo = remember(key1 = workInofs) {
        workInofs?.find {
            it.id == viewModel.getUUIDByBookId(bookId)
        }
    }

    val imageUri by derivedStateOf {
        downloadInfo?.outputData?.getString(WorkerConstant.FILE_URI)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Prototype") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.MainScreen.route) {
                            popUpTo(Screen.MainScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->

        when (bookDetails) {
            is Resource.Success -> {
                bookDetails.data?.let { bookDetails ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(horizontal = 10.dp)
                            .fillMaxSize()
                    ) {
                        BookDetails(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            bookDetail = bookDetails
                        )
                        when (bookState) {
                            is BookState.Download -> {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        if (imageUri != null) {
                                            navController.navigate(
                                                Screen.ReadPdfScreen.passBookUrl(imageUri!!.replaceFS())
                                            )
                                        } else {
                                            viewModel.downloadBook(bookDetails)
                                        }
                                    },
                                    enabled = downloadInfo?.state != WorkInfo.State.RUNNING
                                ) {
                                    when (downloadInfo?.state) {
                                        WorkInfo.State.SUCCEEDED -> {
                                            Text(text = "Read")
                                        }
                                        WorkInfo.State.BLOCKED -> {
                                            Text(text = "Please wait...")
                                        }
                                        WorkInfo.State.RUNNING -> {
                                            Text(text = "Downloading...")
                                        }
                                        WorkInfo.State.FAILED -> {
                                            Text(text = "Error!!!")
                                        }
                                        WorkInfo.State.CANCELLED -> {
                                            Text(text = "Download Cancelled")
                                        }
                                        WorkInfo.State.ENQUEUED -> {
                                            Text(text = "Start Downloading")
                                        }
                                        else -> {
                                            Text(text = "Download")
                                        }
                                    }
                                }
                            }
                            is BookState.Read -> {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        bookDetails.bookState.data?.let {
                                            navController.navigate(
                                                Screen.ReadPdfScreen.passBookUrl(it.replaceFS())
                                            )
                                        }
                                    },
                                ) {
                                    Text(text = "Read")
                                }
                            }
                        }

                    }
                }

            }
            is Resource.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    bookDetails.message?.let { it -> Text(text = it) }
                    IconButton(onClick = {

                    }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }
                }
            }
            is Resource.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator()
                }
            }
            is Resource.Empty -> {
                Button(onClick = {
//                    viewModel.getBookDetails(bookId)
                }) {
                    Text(text = "Get book details")
                }
            }
        }
    }
}

@Composable
fun BookDetails(modifier: Modifier, bookDetail: BookDetail) {
    Column(modifier = modifier) {
        ListItem(
            leadingContent = {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bookDetail.image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_image),
                    contentDescription = null,
                    alignment = Alignment.Center,
                )
            },
            headlineText = {
                Text(
                    text = bookDetail.title,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingText = {
                Text(
                    text = bookDetail.authors,
                    fontWeight = FontWeight.Bold,
                )
            },
            overlineText = {
                Text(text = bookDetail.year)
            }
        )
        Text(
            text = "Book Description",
            style = appTypography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = bookDetail.description,
            style = appTypography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

/**
 * navigate to next screen
 * val url = bookDetails.url.replace("/", "_")
 * navController.navigate(route = Screen.ReadPdfScreen.passBookUrl(url))
 */