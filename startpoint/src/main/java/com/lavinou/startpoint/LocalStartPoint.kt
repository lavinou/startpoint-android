package com.lavinou.startpoint

import androidx.compose.runtime.staticCompositionLocalOf

val LocalStartPoint = staticCompositionLocalOf {
    NoOptStartPoint() as StartPoint
}