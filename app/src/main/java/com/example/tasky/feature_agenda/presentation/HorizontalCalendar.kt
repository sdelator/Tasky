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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import java.time.LocalDate
import java.util.Locale

@Composable
fun HorizontalCalendar(year: Int, month: Int, day: Int) {
    val calendarDays = getCalendarDaysForMonth(year = year, month = month)
    val selectedDate = remember { mutableStateOf<Int?>(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(day) {
        lazyListState.scrollToItem(day - 1) // subtract 1 because indices start at 0
    }

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            state = lazyListState
        ) {
            items(calendarDays) { day ->
                CalendarDayItem(
                    day = day,
                    isSelected = selectedDate.value == day.date,
                    onClick = {
                        selectedDate.value = day.date
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
    HorizontalCalendar(year = 2024, month = 7, day = 9)
}

fun getCalendarDaysForMonth(year: Int, month: Int): List<CalendarDay> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = firstDayOfMonth.lengthOfMonth()

    val calendarDays = mutableListOf<CalendarDay>()
    for (date in 1..daysInMonth) {
        val currentDate = firstDayOfMonth.withDayOfMonth(date)
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(
            java.time.format.TextStyle.NARROW,
            Locale.getDefault()
        )
        calendarDays.add(CalendarDay(dayOfWeek, date))
    }

    return calendarDays
}

data class CalendarDay(val dayOfWeek: String, val date: Int)
