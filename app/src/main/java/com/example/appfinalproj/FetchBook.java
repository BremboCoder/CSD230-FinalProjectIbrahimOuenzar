package com.example.appfinalproj;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FetchBook {

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    private Consumer<String> mOnPostExecute;

    FetchBook(Consumer<String> onPostExecute) {
        this.mOnPostExecute = onPostExecute;
    }
    FetchBook(TextView titleText, TextView authorText) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
    }

    void execute(String queryString) {
        Log.d("FetchBook", "Starting network request...");
        CompletableFuture.supplyAsync(() -> NetworkUtils.getBookInfo(queryString))
                .thenAcceptAsync(mOnPostExecute);
    }

    protected void onPostExecute(String s) {
        Log.d("FetchBook", "Received network response: " + s);
        if (s == null) {
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
            return;
        }
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting
            // when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                Log.d("FetchBook", "Processing item: " + i);
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors"); // Change this line
                    authors = authorsArray.join(", "); // Convert JSONArray to a comma-separated string
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null) {
                Log.d("FetchBook", "Updating UI with title and authors: " + title + ", " + authors);
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            } else {
                // If none are found, update the UI to
                // show failed results.
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
        }
    }
}
