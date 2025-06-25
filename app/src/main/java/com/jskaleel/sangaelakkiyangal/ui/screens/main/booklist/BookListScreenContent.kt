package com.jskaleel.sangaelakkiyangal.ui.screens.main.booklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksEvent
import com.jskaleel.sangaelakkiyangal.ui.theme.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreenContent(
    onBack: CallBack,
    books: List<BookUiModel>,
    subCategory: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = subCategory,
                        fontFamily = fontFamily,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable { onBack() }
                            .padding(8.dp)
                    )
                }
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(books) { book ->
                Text(
                    book.title,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Immutable
data class BookUiModel(
    val title: String,
    val id: String,
    val url: String
)