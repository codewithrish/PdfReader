package com.codewithrish.pdfreader.ui.screen.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ToolsList(
    tools: List<Tool>,
    onToolClick: (ToolType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tools.size) {index ->
                ToolsItem(
                    image = tools[index].image!!,
                    contentDescription = tools[index].contentDescription,
                    imageSize = tools[index].imageSize,
                    featureName = tools[index].featureName,
                    modifier = tools[index].modifier
                        .clickable {
                            onToolClick(tools[index].toolType)
                        },
                )
            }
        }
    }
}

@Composable
fun ToolsItem(
    image: Int,
    contentDescription: String? = null,
    imageSize: Dp,
    featureName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(imageSize)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                .border(width = 0.dp, color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = contentDescription,
                modifier = Modifier.size(imageSize).padding(8.dp)
            )
        }
        Text(
            text = featureName,
            style = MaterialTheme.typography.labelLarge,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(top = 8.dp)
        )
    }
}
