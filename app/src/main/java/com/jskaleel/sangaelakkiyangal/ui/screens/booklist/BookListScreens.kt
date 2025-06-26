package com.jskaleel.sangaelakkiyangal.ui.screens.booklist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jskaleel.sangaelakkiyangal.core.CallBack
import com.jskaleel.sangaelakkiyangal.ui.theme.AppTheme
import com.jskaleel.sangaelakkiyangal.ui.theme.fontFamily
import com.jskaleel.sangaelakkiyangal.ui.utils.ProvideAppBarNavigationIcon
import com.jskaleel.sangaelakkiyangal.ui.utils.ProvideAppBarTitle

@Composable
fun BookListScreenContent(
    onBack: CallBack,
    books: List<BookUiModel>,
    subCategory: String,
    event: (BookListEvent) -> Unit,
) {
    ProvideAppBarTitle {
        Text(
            text = subCategory,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
    }

    ProvideAppBarNavigationIcon {
        IconButton(
            modifier = Modifier.clip(shape = CircleShape),
            onClick = { onBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(books) { book ->
            BookListItem(
                book = book,
                onDownloadClick = {
                    event.invoke(BookListEvent.OnDownloadClick(it))
                },
                onOpenClick = {
                    event.invoke(BookListEvent.OnOpenClick(it))
                }
            )
        }
    }
}

@Composable
fun BookListItem(
    book: BookUiModel,
    onDownloadClick: (String) -> Unit,
    onOpenClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (book.downloaded) onOpenClick(book.id)
                    else onDownloadClick(book.id)
                }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = book.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(onClick = {
                if (book.downloaded) onOpenClick(book.id)
                else onDownloadClick(book.id)
            }) {
                Icon(
                    imageVector = if (book.downloaded) Icons.AutoMirrored.Rounded.MenuBook else Icons.Rounded.Download,
                    contentDescription = if (book.downloaded) "Open Book" else "Download Book"
                )
            }
        }

        AnimatedContent(
            targetState = book.progress,
            label = "progress_anim",
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        ) { state ->
            if (state) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(),
                    strokeCap = StrokeCap.Round
                )
            } else {
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline,
    )
}


@Immutable
data class BookUiModel(
    val title: String,
    val id: String,
    val url: String,
    val downloaded: Boolean,
    val progress: Boolean,
)

@Preview(showBackground = true)
@Composable
private fun BookListScreenContentPreview() {
    AppTheme {
        val books = listOf(
            BookUiModel(
                title = "Book 1",
                id = "1",
                url = "url1",
                downloaded = false,
                progress = false
            ),
            BookUiModel(
                title = "Book 2",
                id = "2",
                url = "url2",
                downloaded = true,
                progress = true,
            )
        )
        BookListScreenContent(
            onBack = {},
            books = books,
            subCategory = "SubCategory",
            event = { }
        )
    }
}
