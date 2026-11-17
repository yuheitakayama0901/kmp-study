package com.example.kmpstudy.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmpstudy.platformName
import com.example.kmpstudy.theme.AppTheme

private val ScreenPadding = 20.dp

@Composable
fun TodoScreen(viewModel: TodoViewModel = viewModel { TodoViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val colors = AppTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = ScreenPadding, end = ScreenPadding, bottom = 32.dp),
        ) {
            item(key = "header") {
                Header(remaining = state.remainingCount, total = state.todos.size)
            }
            item(key = "input") {
                InputCard(
                    text = state.input,
                    canAdd = state.canAdd,
                    onTextChange = viewModel::onInputChange,
                    onAdd = viewModel::add,
                )
                Spacer(Modifier.height(20.dp))
            }
            item(key = "filter") {
                SegmentedFilter(current = state.filter, onSelect = viewModel::setFilter)
                Spacer(Modifier.height(16.dp))
            }

            val todos = state.visibleTodos
            if (todos.isEmpty()) {
                item(key = "empty") { EmptyState(state.filter) }
            }
            itemsIndexed(todos, key = { _, todo -> todo.id }) { index, todo ->
                TodoRow(
                    todo = todo,
                    isFirst = index == 0,
                    isLast = index == todos.lastIndex,
                    onToggle = { viewModel.toggle(todo.id) },
                    onDelete = { viewModel.delete(todo.id) },
                    modifier = Modifier.animateItem(),
                )
            }

            if (state.todos.any { it.done }) {
                item(key = "clear") {
                    Box(Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "完了したものを消す",
                            color = colors.textMuted,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable(onClick = viewModel::clearDone)
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(remaining: Int, total: Int) {
    val colors = AppTheme.colors
    Column(Modifier.padding(top = 24.dp, bottom = 20.dp)) {
        Text(
            text = platformName(),
            color = colors.accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "やること",
            color = colors.text,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = when {
                total == 0 -> "まだ何もありません"
                remaining == 0 -> "全部おわりました"
                else -> "残り $remaining 件 / 全 $total 件"
            },
            color = colors.textMuted,
            fontSize = 15.sp,
        )
    }
}

@Composable
private fun InputCard(
    text: String,
    canAdd: Boolean,
    onTextChange: (String) -> Unit,
    onAdd: () -> Unit,
) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.surface)
            .padding(start = 18.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.weight(1f)) {
            if (text.isEmpty()) {
                Text("新しいやることを追加", color = colors.textFaint, fontSize = 16.sp)
            }
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true,
                textStyle = TextStyle(color = colors.text, fontSize = 16.sp),
                cursorBrush = SolidColor(colors.accent),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAdd() }),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        val bg by animateColorAsState(if (canAdd) colors.accent else colors.surfaceMuted)
        val fg by animateColorAsState(if (canAdd) colors.onAccent else colors.textFaint)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(bg)
                .clickable(enabled = canAdd, onClick = onAdd),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "追加", tint = fg)
        }
    }
}

/** iOS のセグメントコントロール風。選択中の背景が横にスライドする */
@Composable
private fun SegmentedFilter(current: TodoFilter, onSelect: (TodoFilter) -> Unit) {
    val colors = AppTheme.colors
    val options = TodoFilter.entries
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceMuted)
            .padding(3.dp),
    ) {
        val segmentWidth = maxWidth / options.size
        val offset by animateDpAsState(
            targetValue = segmentWidth * current.ordinal,
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
        )
        Box(
            Modifier
                .offset(x = offset)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(9.dp))
                .background(colors.surface),
        )
        Row(Modifier.fillMaxSize()) {
            options.forEach { option ->
                val selected = option == current
                val textColor by animateColorAsState(if (selected) colors.text else colors.textMuted)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSelect(option) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option.label,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun TodoRow(
    todo: Todo,
    isFirst: Boolean,
    isLast: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(
        topStart = if (isFirst) 18.dp else 0.dp,
        topEnd = if (isFirst) 18.dp else 0.dp,
        bottomStart = if (isLast) 18.dp else 0.dp,
        bottomEnd = if (isLast) 18.dp else 0.dp,
    )
    val textColor by animateColorAsState(if (todo.done) colors.textFaint else colors.text)

    Column(modifier.clip(shape).background(colors.surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(start = 16.dp, end = 6.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundCheck(checked = todo.done)
            Spacer(Modifier.width(14.dp))
            Text(
                text = todo.title,
                color = textColor,
                fontSize = 16.sp,
                textDecoration = if (todo.done) TextDecoration.LineThrough else null,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onDelete),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "削除",
                    tint = colors.textFaint,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        if (!isLast) {
            Box(
                Modifier
                    .padding(start = 52.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.divider),
            )
        }
    }
}

/** 丸いチェック。チェックすると塗りつぶされてチェックマークがポンと出る */
@Composable
private fun RoundCheck(checked: Boolean) {
    val colors = AppTheme.colors
    val fill by animateColorAsState(if (checked) colors.accent else Color.Transparent)
    val stroke by animateColorAsState(if (checked) colors.accent else colors.textFaint)
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(fill)
            .border(1.8.dp, stroke, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                tint = colors.onAccent,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun EmptyState(filter: TodoFilter) {
    val colors = AppTheme.colors
    val message = when (filter) {
        TodoFilter.All -> "やることはありません"
        TodoFilter.Active -> "未完了のものはありません"
        TodoFilter.Done -> "完了したものはまだありません"
    }
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(message, color = colors.textFaint, fontSize = 15.sp)
    }
}
