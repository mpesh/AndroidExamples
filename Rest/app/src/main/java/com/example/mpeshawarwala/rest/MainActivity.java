package com.example.mpeshawarwala.rest;

        import android.app.Activity;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
        import org.springframework.web.client.RestTemplate;
        import org.springframework.http.client.SimpleClientHttpRequestFactory;

        import java.net.InetSocketAddress;
        import java.net.Proxy;

public class MainActivity extends ActionBarActivity {
//public class MainActivity extends Activity {

    private static final String PXHOST = "10.3.0.58";
    private static final String PXPORT = "8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
                InetSocketAddress address = new InetSocketAddress(PXHOST, Integer.parseInt(PXPORT));
                Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
                factory.setProxy(proxy);

                final String url = "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(factory);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Greeting greeting = restTemplate.getForObject(url, Greeting.class);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            greetingIdText.setText(greeting.getId());
            greetingContentText.setText(greeting.getContent());
        }

    }

}