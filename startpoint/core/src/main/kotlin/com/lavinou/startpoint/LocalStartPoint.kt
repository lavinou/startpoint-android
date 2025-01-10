package com.lavinou.startpoint

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalStartPoint: ProvidableCompositionLocal<StartPoint> = staticCompositionLocalOf {
    NoOptStartPoint()
}