package com.jskaleel.sangaelakkiyangal.ui.model

import androidx.annotation.DrawableRes

data class BottomBarItem(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
)