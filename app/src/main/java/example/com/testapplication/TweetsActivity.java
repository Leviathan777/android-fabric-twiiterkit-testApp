package example.com.testapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

/**
 * Created by dns on 21.01.15.
 */
public class TweetsActivity extends ListActivity {
    private TweetViewAdapter adapter;
    private Button twSentTweet;
    private Button twLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        adapter = new TweetViewAdapter(this);
        twSentTweet = (Button)findViewById(R.id.twSendTweet);
        twLogoutButton = (Button)findViewById(R.id.twLogout);
        twSentTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTweet();
            }
        });

        twLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Twitter.getSessionManager().clearActiveSession();
                finish();
            }
        });
        setListAdapter(adapter);
        loadTweets();
    }

    public void loadTweets() {
        final StatusesService service = Twitter.getInstance().getApiClient().getStatusesService();

        service.homeTimeline(null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        adapter.setTweets(result.data);
                    }

                    @Override
                    public void failure(TwitterException error) {
                        Toast.makeText(TweetsActivity.this, "Failed to retrieve timeline",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void publishTweet() {
        final StatusesService statusesService = Twitter.getInstance().getApiClient().getStatusesService();
        statusesService.update("Привет хабр!", null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> tweetResult) {
                Toast.makeText(TweetsActivity.this, "Успешно опубликовали статус",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(TweetsActivity.this, "Ошибка при отправке твита",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            publishTweet();
            return true;
        }else if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

