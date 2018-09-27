package com.juancoob.nanodegree.and.vegginner;

import com.juancoob.nanodegree.and.vegginner.ui.MainActivity;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Juan Antonio Cobos Obrero on 27/09/18.
 */

@RunWith(RobolectricTestRunner.class)
public class BeginningTest {

    private MainActivity mainActivity;

    @Before
    public void setup() throws Exception{
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(mainActivity);
    }

    @Test
    public void shouldHaveBeginningFragment() throws Exception {
        assertNotNull(mainActivity.getSupportFragmentManager().findFragmentByTag(Constants.BEGINNING));
    }

}
