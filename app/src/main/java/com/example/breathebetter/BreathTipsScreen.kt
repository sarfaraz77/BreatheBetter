package com.example.breathebetter

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathebetter.data.BreathTipRepository
import com.example.breathebetter.model.BreathTip
import com.example.breathebetter.ui.theme.BreatheBetterTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BreathTipList(
    breathTips: List<BreathTip>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
    LazyColumn(contentPadding = contentPadding) {
        itemsIndexed(breathTips) { index, breathTip ->
            BreathTipCard(
                breathTip = breathTip,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .animateEnterExit(
                        enter = slideInVertically(
                            animationSpec = spring(
                                stiffness = StiffnessLow,
                                dampingRatio = DampingRatioLowBouncy
                            ),
                            initialOffsetY = { it * (index + 1) }
                        )
                    )
            )
        }
        }
    }
}

@Composable
fun BreathTipCard(
    breathTip: BreathTip,
    modifier: Modifier = Modifier) {
    var expanded by remember {mutableStateOf(false)}
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.primaryContainer,
    )
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .background(color = color)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .sizeIn(minHeight = 104.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(breathTip.cardNumber),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(92.dp)
                    ) {
                        Image(
                            painter = painterResource(breathTip.imageRes),
                            contentDescription = null,
                            alignment = Alignment.TopCenter,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(breathTip.titleRes),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                DescriptionButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded}
                )
            }
            if(expanded) {
                LoadDescription(
                    loadDescription = breathTip.descriptionRes,
                    modifier = Modifier.padding(12.dp)
                )
            }

        }
    }
}

@Composable
fun LoadDescription(
    @StringRes loadDescription: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text (
            text = stringResource(loadDescription),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DescriptionButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.tip1_title),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}



@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BreathTipCardPreview() {
    val breathTip = BreathTip(
        R.string.tip1_card_number,
        R.string.tip1_title,
        R.string.tip1_description,
        R.drawable.allergy3
    )
    BreatheBetterTheme{
        BreathTipCard(breathTip = breathTip)
    }
}

@Preview("BreathTips List")
@Composable
fun BreathTipsListPreview() {
    BreatheBetterTheme(darkTheme = false){
        Surface (
            color = MaterialTheme.colorScheme.background
        ) {
            BreathTipList(breathTips = BreathTipRepository.breathTips)
        }
    }
}