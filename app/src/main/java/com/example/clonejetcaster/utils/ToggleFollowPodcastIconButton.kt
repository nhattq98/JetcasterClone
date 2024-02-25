package com.example.clonejetcaster.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToggleFollowPodcastIconButton(
    isFollowed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = when {
                isFollowed -> Icons.Default.Check
                else -> Icons.Default.Add
            },
            contentDescription = null,
            tint = animateColorAsState(
                when {
                    isFollowed -> LocalContentColor.current
                    else -> Color.Black.copy(alpha = 0.5f)
                }
            ).value,
            modifier = Modifier
                .shadow(
                    elevation = animateDpAsState(if (isFollowed) 0.dp else 1.dp).value,
                    shape = MaterialTheme.shapes.small
                )
                .background(
                    color = animateColorAsState(
                        when {
                            isFollowed -> MaterialTheme.colorScheme.surface.copy(0.38f)
                            else -> Color.White
                        }
                    ).value,
                    shape = MaterialTheme.shapes.small
                )
                .padding(4.dp)

        )
    }

}