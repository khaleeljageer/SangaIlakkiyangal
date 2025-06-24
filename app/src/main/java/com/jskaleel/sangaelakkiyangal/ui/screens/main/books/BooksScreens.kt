package com.jskaleel.sangaelakkiyangal.ui.screens.main.books

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BooksScreenContent(
    event: (BooksEvent) -> Unit,
    categories: List<CategoryUiModel>
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            ExpandableCategorySection(
                category = category,
                onToggleExpand = {
                    event.invoke(BooksEvent.OnCategoryToggle(index))
                },
                onSubCategoryClick = { }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BooksLoadingScreen() {

}

@Composable
fun BooksEmptyScreen() {

}

@Composable
fun ExpandableCategorySection(
    category: CategoryUiModel,
    onToggleExpand: () -> Unit,
    onSubCategoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = if (category.isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null
            )
        }

        if (category.isExpanded) {
            FlowRow(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                category.subCategories.forEach {
                    SubCategoryItem(subCategory = it, onClick = onSubCategoryClick)
                }
            }
        }
    }
}

@Composable
private fun SubCategoryItem(subCategory: SubCategoryUiModel, onClick: () -> Unit) {
    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = subCategory.title,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    )
}


@Stable
data class CategoryUiModel(
    val title: String,
    val isExpanded: Boolean = false,
    val subCategories: List<SubCategoryUiModel> = emptyList()
)

@Stable
data class SubCategoryUiModel(
    val title: String,
)