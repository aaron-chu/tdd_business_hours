package com.example.myapplication

import java.util.*
import java.util.concurrent.TimeUnit

class BusinessService(private vararg val businessHours: BusinessHours) {
    fun getBusinessState(currentCalendar: Calendar): BusinessState =
        businessHours.fold(mutableListOf(), appendCalendarPeriod(currentCalendar))
            .map { calendarPeriod -> getBusinessState(currentCalendar, calendarPeriod) }
            .reduce(::combineBusinessState)

    private fun appendCalendarPeriod(currentCalendar: Calendar) =
        { calendarPeriods: MutableList<CalendarPeriod>, businessHours: BusinessHours ->
            val periods = convertToCalendarPeriods(currentCalendar, businessHours)
            calendarPeriods.apply { addAll(periods) }
        }

    private fun convertToCalendarPeriods(
        currentCalendar: Calendar,
        businessHours: BusinessHours
    ): MutableList<CalendarPeriod> {
        val currentWeek = currentCalendar.get(Calendar.DAY_OF_WEEK) - 1

        val calendarPeriod = getCalendarPeriod(currentCalendar, businessHours, currentWeek)
        val nextCalendarPeriod = getNextCalendarPeriod(calendarPeriod)

        return mutableListOf<CalendarPeriod>().apply {
            add(calendarPeriod)
            add(nextCalendarPeriod)
        }
    }

    private fun getNextCalendarPeriod(calendarPeriod: CalendarPeriod): CalendarPeriod {
        val startCalendar = Calendar.getInstance().apply {
            this.timeInMillis = calendarPeriod.start.timeInMillis
            add(Calendar.WEEK_OF_YEAR, 1)
        }

        val endCalendar = Calendar.getInstance().apply {
            this.timeInMillis = calendarPeriod.end.timeInMillis
            add(Calendar.WEEK_OF_YEAR, 1)
        }

        return CalendarPeriod(startCalendar, endCalendar)
    }

    private fun getCalendarPeriod(
        currentCalendar: Calendar,
        businessHours: BusinessHours,
        currentWeek: Int
    ): CalendarPeriod {
        val startCalendar = getCalendar(
            currentCalendar,
            businessHours.start,
            -diffInDays(businessHours.start.week, currentWeek)
        )

        val endCalendar = getCalendar(
            startCalendar,
            businessHours.end,
            diffInDays(businessHours.start.week, businessHours.end.week)
        )

        if (startCalendar.after(endCalendar)) {
            startCalendar.add(Calendar.WEEK_OF_YEAR, -1)
        }

        return CalendarPeriod(startCalendar, endCalendar)
    }

    private fun getCalendar(
        currentCalendar: Calendar,
        businessHour: BusinessHour,
        days: Int
    ): Calendar = Calendar.getInstance().apply {
        this.timeInMillis = currentCalendar.timeInMillis

        set(Calendar.HOUR_OF_DAY, businessHour.hour)
        set(Calendar.MINUTE, businessHour.min)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        add(Calendar.DAY_OF_MONTH, days)
    }

    private fun diffInDays(startWeek: Int, endWeek: Int): Int = when {
        startWeek < endWeek -> endWeek - startWeek
        startWeek > endWeek -> (endWeek + 7) - startWeek
        else -> 0
    }

    private fun getBusinessState(
        currentCalendar: Calendar,
        calendarPeriod: CalendarPeriod
    ): BusinessState {
        val startDiffInMillis = calendarPeriod.start.diffInMillis(currentCalendar)
        val endDiffInMillis = calendarPeriod.end.diffInMillis(currentCalendar)

        val startDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(startDiffInMillis)
        val endDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(endDiffInMillis)

        return when {
            startDiffInMinutes < -60 -> BusinessState.CLOSE
            startDiffInMinutes >= -60 && startDiffInMinutes < 0 -> BusinessState.OPEN_SOON
            startDiffInMinutes >= 0 && endDiffInMinutes < -60 -> BusinessState.OPEN
            endDiffInMinutes >= -60 && endDiffInMinutes < 0 -> BusinessState.CLOSE_SOON
            endDiffInMinutes >= 0 -> BusinessState.CLOSE
            else -> BusinessState.CLOSE
        }
    }

    private fun combineBusinessState(
        previousState: BusinessState,
        nextState: BusinessState
    ): BusinessState = when (previousState) {
        BusinessState.CLOSE -> nextState
        BusinessState.OPEN -> BusinessState.OPEN
        BusinessState.CLOSE_SOON -> BusinessState.CLOSE_SOON
        BusinessState.OPEN_SOON -> {
            if (nextState == BusinessState.CLOSE_SOON) {
                BusinessState.CLOSE_SOON
            } else {
                BusinessState.OPEN_SOON
            }
        }
    }
}

private fun Calendar.diffInMillis(another: Calendar): Long = another.timeInMillis - timeInMillis
