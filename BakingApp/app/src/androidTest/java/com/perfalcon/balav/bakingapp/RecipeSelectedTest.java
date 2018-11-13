package com.perfalcon.balav.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.perfalcon.balav.bakingapp.MainActivity;
import com.perfalcon.balav.bakingapp.RecipeAdapter;
import com.perfalcon.balav.bakingapp.model.Baking;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeSelectedTest {


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;
    Baking baking;
    int no_recipes;

    public void prepBakingRecipe(){
        baking =activityRule.getActivity ().mBaking.get (1);
    }

    @Before
    public void setUp(){
        no_recipes = 4;
    }
    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void OnRecipesDisplayedTest(){
        onView (ViewMatchers.withId (R.id.rv_recipe))
                .perform (RecyclerViewActions.actionOnItemAtPosition (3,scrollTo ()))
                .check (matches (hasDescendant (withText ("Yellow Cake"))));
    }

    @Test
    public void OnRecipeNoBlankTest(){
        onView (ViewMatchers.withId (R.id.rv_recipe))
                .perform (RecyclerViewActions.scrollToPosition (3))
                .check (matches (not(withText (""))));

        for(int i=1;i<=no_recipes;i++){
            onView (ViewMatchers.withId (R.id.rv_recipe))
                    .perform (RecyclerViewActions.scrollToPosition (i))
                    .check (matches (not(withText (""))));
        }
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }


    public static Matcher<RecyclerView.ViewHolder> withHolderRecipe(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeViewHolder>(RecipeAdapter.RecipeViewHolder.class) {

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder item) {
                Log.v("In Matcher","matchSafely");
                TextView recipeText = item.itemView.findViewById(R.id.text_recipe);
                if (recipeText == null) {
                    return false;
                }
                return recipeText.getText().toString().contains(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }
        };
    }





}
