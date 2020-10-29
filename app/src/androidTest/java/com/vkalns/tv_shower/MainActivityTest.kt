package com.vkalns.tv_shower

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TITLE = "Game of Thrones"

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var main: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)


    private var okhttpIdlingResource: OkHttp3IdlingResource? = null

    @Before
    fun setup() {
        // adding idling resource
        okhttpIdlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpClient())
        IdlingRegistry.getInstance().register(
            okhttpIdlingResource
        )

    }

    @Test
    fun typeInSearchBar_clickSearchButton_checkTitleMatches() {
        // Types a title in the search bar
        onView(withId(R.id.search_bar))
            .perform(typeText(TITLE))

        // Clicks a search button
        onView(withId(R.id.search_button)).perform(click())

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        // Checks if the title matches
        onView(withId(R.id.show_title)).check(matches(withText(TITLE)))

    }

    @After
    fun whenFinished() {
        // Things added in setup() needs to get unregistered
        IdlingRegistry.getInstance().unregister(okhttpIdlingResource)
    }
}
