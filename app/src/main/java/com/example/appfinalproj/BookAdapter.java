//Ibrahim Ouenzar
//CSD230
//Final Project

package com.example.appfinalproj;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<ResultsActivity.Book> books;

    public BookAdapter(List<ResultsActivity.Book> books) {
        this.books = books;
    }



    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        ResultsActivity.Book book = books.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorsTextView.setText(book.getAuthors());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView authorsTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorsTextView = itemView.findViewById(R.id.authorsTextView);
        }
    }
}
