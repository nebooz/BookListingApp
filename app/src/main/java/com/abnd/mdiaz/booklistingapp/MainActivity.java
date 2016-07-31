package com.abnd.mdiaz.booklistingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String BOOKS_QUERY_INIT =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final String BOOKS_MAX_RESULTS_STRING = "&maxResults=";

    private static final int BOOKS_MAX_RESULTS_VALUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bookSearch(View view) {

        EditText inputText = (EditText) findViewById(R.id.edit_text);
        String text = String.valueOf(inputText.getText());

        BooksAsyncTask task = new BooksAsyncTask(text);
        task.execute();
    }

    public void fillBookList(ArrayList<Book> books) {
        ListView bookListView = (ListView) findViewById(R.id.list);

        BookAdapter adapter = new BookAdapter(this, books);

        bookListView.setAdapter(adapter);
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.i(LOG_TAG, stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private Book extractFeatureFromJson(String bookJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray itemArray = baseJsonResponse.getJSONArray("items");

            // If there are results in the features array
            if (itemArray.length() > 0) {
                // Extract out the first feature (which is an earthquake)
                JSONObject item = itemArray.getJSONObject(0);
                JSONObject volume = item.getJSONObject("volumeInfo");
                JSONArray authors = volume.optJSONArray("authors");
                //JSONObject properties = item.getJSONObject("properties");

                // Extract out the title, time, and tsunami values
                String title = volume.getString("title");
                String description = volume.optString("description");

                if (Objects.equals(description, "")) {
                    description = "No description available.";
                }

                String authorsList = "No authors defined.";

                if (authors != null) {
                    StringBuilder authorsBuilder = new StringBuilder();
                    for (int i = 0; i < authors.length(); i++) {
                        authorsBuilder.append(authors.getString(i));
                        if (i != authors.length() - 1) {
                            authorsBuilder.append(", ");
                        }
                    }
                    authorsList = authorsBuilder.toString();
                }

                // Create a new {@link Event} object
                return new Book(title, authorsList, description);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return null;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public class BooksAsyncTask extends AsyncTask<URL, Void, Book> {

        private String mText;

        public BooksAsyncTask(String text) {
            mText = text;
        }

        @Override
        protected Book doInBackground(URL... urls) {

            StringBuilder query = new StringBuilder();
            query.append(BOOKS_QUERY_INIT);
            try {
                //Now the user can add blank spaces and other non-lettery stuff.
                query.append(URLEncoder.encode(mText, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            query.append(BOOKS_MAX_RESULTS_STRING);
            query.append(BOOKS_MAX_RESULTS_VALUE);

            // Create URL object
            URL url = createUrl(String.valueOf(query));

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";

            try {

                jsonResponse = makeHttpRequest(url);

            } catch (IOException e) {
                // TODO Handle the IOException
            }

            Book book = extractFeatureFromJson(jsonResponse);

            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            ArrayList<Book> books = new ArrayList<>();

            books.add(book);

            fillBookList(books);
        }
    }
}
