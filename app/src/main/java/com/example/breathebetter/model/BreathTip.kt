package com.example.breathebetter.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BreathTip(
    @StringRes val cardNumber: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
