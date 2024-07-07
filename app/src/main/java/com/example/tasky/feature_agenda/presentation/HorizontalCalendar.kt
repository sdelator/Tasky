
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import java.time.LocalDate

@Composable
fun HorizontalCalendar(
    calendarDays: List<CalendarDay>,
    year: Int,
    month: Int,
    day: Int,
    updateDateSelected: (LocalDate) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(day) {
        val layoutInfo = lazyListState.layoutInfo
        val visibleItems = layoutInfo.visibleItemsInfo
        val targetIndex = day - 1 // subtract 1 because indices start at 0

        if (!visibleItems.any { it.index == targetIndex }) {
            lazyListState.scrollToItem(targetIndex)
        }
    }

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            state = lazyListState
        ) {
            items(calendarDays) { calendarDay ->
                CalendarDayItem(
                    day = calendarDay,
                    isSelected = day == calendarDay.date,
                    onClick = {
                        val date = LocalDate.of(year, month, calendarDay.date)
                        updateDateSelected(date)
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .background(
                if (isSelected) colorResource(id = R.color.orange_highlight) else Color.Transparent,
                shape = RoundedCornerShape(30.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeekdayText(dayOfWeek = day.dayOfWeek, isSelected = isSelected)
        DateText(date = day.date)
    }
}

@Composable
fun WeekdayText(dayOfWeek: String, isSelected: Boolean) {
    Text(
        text = dayOfWeek,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) colorResource(id = R.color.dark_gray) else colorResource(id = R.color.gray),
        modifier = Modifier.padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 2.dp)
    )
}

@Composable
fun DateText(date: Int) {
    Text(
        text = date.toString(),
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.dark_gray),
        modifier = Modifier.padding(top = 2.dp, start = 15.dp, end = 15.dp, bottom = 10.dp)
    )
}

@Composable
@Preview
fun PreviewHorizontalCalendar() {
    HorizontalCalendar(
        calendarDays = listOf(),
        year = 2024,
        month = 7,
        day = 9,
        updateDateSelected = { })
}