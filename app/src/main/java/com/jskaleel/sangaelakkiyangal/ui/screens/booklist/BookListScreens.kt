package com.jskaleel.sangaelakkiyangal.ui.screens.booklist

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (book.downloading) {
            ShimmerProgressBar(
                modifier = Modifier.matchParentSize(),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (!book.downloading) {
                        if (book.downloaded) {
                            onOpenClick(book.id)
                        } else {
                            onDownloadClick(book.id)
                        }
                    }
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

            when {
                book.downloading -> {
                    IconButton(onClick = {}) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                else -> {
                    if (book.downloaded) {
                        IconButton(onClick = { onOpenClick(book.id) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                                contentDescription = "Open Book"
                            )
                        }
                    } else {
                        IconButton(onClick = { onDownloadClick(book.id) }) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = "Download Book"
                            )
                        }
                    }
                }
            }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline,
    )
}

@Composable
private fun ShimmerProgressBar(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val shimmerX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer-x"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        ),
        start = Offset.Zero,
        end = Offset(shimmerX, shimmerX)
    )

    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .background(shimmerBrush)
        )
    )
}

@Immutable
data class BookUiModel(
    val title: String,
    val id: String,
    val url: String,
    val progress: Int = 0,
    val downloaded: Boolean,
    val downloading: Boolean,
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
                downloading = false
            ),
            BookUiModel(
                title = "Book 2",
                id = "2",
                url = "url2",
                downloaded = false,
                downloading = true,
                progress = 50
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
