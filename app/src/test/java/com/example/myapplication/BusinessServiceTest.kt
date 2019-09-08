package com.example.myapplication

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class BusinessServiceTest {

    @Test
    fun `given one business hours in same day, when 61 mins before start, then it's close`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 8, 59)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE, businessState)
    }

    @Test
    fun `given one business hours in same day, when 60 mins before start, then it's open soon`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 9, 0)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN_SOON, businessState)
    }

    @Test
    fun `given one business hours in same day, when 1 min before start, then it's open soon`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 9, 59)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN_SOON, businessState)
    }

    @Test
    fun `given one business hours in same day, when current time is the same as start, then it's open`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 10, 0)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN, businessState)
    }

    @Test
    fun `given one business hours in same day, when 61 mins before end, then it's open`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 18, 59)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN, businessState)
    }

    @Test
    fun `given one business hours in same day, when 60 mins before end, then it's close soon`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 19, 0)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE_SOON, businessState)
    }

    @Test
    fun `given one business hours in same day, when 1 min before end, then it's close soon`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 19, 59)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE_SOON, businessState)
    }

    @Test
    fun `given one business hours in same day, when the current is the same as end, then it's close`() {
        val givenBusinessHours = BusinessHours(
            start = BusinessHour(
                week = 1,
                hour = 10,
                min = 0
            ),
            end = BusinessHour(
                week = 1,
                hour = 20,
                min = 0
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 20, 0)

        val businessState =
            BusinessService(givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE, businessState)
    }

    @Test
    fun `given two business hours in same day, when 30 mins before first end and 60 mins before second start, then it's close soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 2,
                    hour = 9,
                    min = 0
                ),
                end = BusinessHour(
                    week = 2,
                    hour = 16,
                    min = 30
                )
            ),
            BusinessHours(
                start = BusinessHour(
                    week = 2,
                    hour = 17,
                    min = 0
                ),
                end = BusinessHour(
                    week = 2,
                    hour = 23,
                    min = 50
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 10, 16, 0)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE_SOON, businessState)
    }

    @Test
    fun `given two business hours in same day, when the current time is the same as first end and 30 mins before second start, then it's open soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 2,
                    hour = 9,
                    min = 0
                ),
                end = BusinessHour(
                    week = 2,
                    hour = 16,
                    min = 30
                )
            ),
            BusinessHours(
                start = BusinessHour(
                    week = 2,
                    hour = 17,
                    min = 0
                ),
                end = BusinessHour(
                    week = 2,
                    hour = 23,
                    min = 50
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 10, 16, 30)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN_SOON, businessState)
    }

    @Test
    fun `given one business hours which is whole day, when 10 mins before start, then it's open soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 3,
                    hour = 0,
                    min = 0
                ),
                end = BusinessHour(
                    week = 3,
                    hour = 24,
                    min = 0
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 10, 23, 50)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN_SOON, businessState)
    }

    @Test
    fun `given one business hours which crosses day, when 60 mins before end, then it's close soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 6,
                    hour = 22,
                    min = 40
                ),
                end = BusinessHour(
                    week = 0,
                    hour = 0,
                    min = 30
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 14, 23, 30)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE_SOON, businessState)
    }

    @Test
    fun `given one business hours which crosses a whole week, when 60 mins before end, then it's close soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 1,
                    hour = 8,
                    min = 0
                ),
                end = BusinessHour(
                    week = 1,
                    hour = 7,
                    min = 0
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 6, 0)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.CLOSE_SOON, businessState)
    }

    @Test
    fun `given one business hours which crosses a whole week, when 60 mins before start, then it's open soon`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 1,
                    hour = 8,
                    min = 0
                ),
                end = BusinessHour(
                    week = 1,
                    hour = 7,
                    min = 0
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 7, 0)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN_SOON, businessState)
    }

    @Test
    fun `given one business hours which crosses a whole week, when the current time is the same as start, then it's open`() {
        val givenBusinessHours = arrayOf(
            BusinessHours(
                start = BusinessHour(
                    week = 1,
                    hour = 8,
                    min = 0
                ),
                end = BusinessHour(
                    week = 1,
                    hour = 7,
                    min = 0
                )
            )
        )
        val givenCurrentCalendar = getCalendar(2019, 9, 9, 8, 0)

        val businessState =
            BusinessService(*givenBusinessHours).getBusinessState(givenCurrentCalendar)

        assertEquals(BusinessState.OPEN, businessState)
    }

    private fun getCalendar(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        min: Int
    ): Calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, min)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}