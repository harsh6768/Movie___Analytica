package com.technohack.movie_analytica;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignInTest {

    @Rule
    public ActivityTestRule<SignIn> signInActivityTestRule=new ActivityTestRule<>(SignIn.class);
    private SignIn signIn=null;

    @Before
    public void setUp() throws Exception {
        //to get the reference of the activity
        signIn=signInActivityTestRule.getActivity();

    }

    @Test
    public void onTestLaunch(){

        View view=signIn.findViewById(R.id.signIn_layoutId);
        //check whether view is loaded or not
        assertNotNull(view);

    }
    @After
    public void tearDown() throws Exception {

        signIn=null;

    }
}