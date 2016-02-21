package com.udacity.gradle.builditbigger;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.JokesProvider;
import com.robotium.solo.Solo;

import stanislav.volnjanskij.jokedisplay.JokeActivity;

/**
 * Created by Stanislav Volnyansky on 16.02.16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testUI(){
        solo.clickOnView(solo.getView(R.id.tell_joke));
        assertEquals(true, solo.waitForActivity(JokeActivity.class, 5000));
        String joke=new JokesProvider().getJoke();
        TextView jokeView= (TextView) solo.getView(R.id.joke_text);
        assertEquals (true,jokeView.getText().toString().equals(joke));


    }
    String downloadedJoke="";
    public void testAsyncTask(){
        JokeDownloadAsyncTask task=new JokeDownloadAsyncTask();
        String joke=new JokesProvider().getJoke();

        task.setCallback(new JokeDownloadAsyncTask.Callback() {
            @Override
            public void onJoke(String joke) {
                downloadedJoke=joke;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        task.execute();
        solo.sleep(2000);
        assertEquals(true,downloadedJoke.equals(joke));
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
