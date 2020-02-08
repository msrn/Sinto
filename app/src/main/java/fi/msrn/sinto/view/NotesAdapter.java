package fi.msrn.sinto.view;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fi.msrn.sinto.R;
import fi.msrn.sinto.database.models.Note;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.MyviewHolder> {

    private static final String LOG_TAG = "NotesAdapter";

    private Context context;
    private List<Note> notesList;

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView dot;
        public TextView date;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            dot = itemView.findViewById(R.id.dot);
            date = itemView.findViewById(R.id.date);
        }
    }

    public NotesAdapter(Context context, List<Note> notesLists) {
        this.context = context;
        this.notesList = notesLists;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.note.setText(note.getTitle());

        //dot

        holder.dot.setText((Html.fromHtml("&#8226")));

        holder.date.setText(formatDate(note.getDate()));

    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(dateStr);
            SimpleDateFormat formatOut = new SimpleDateFormat("MMM d");
            return formatOut.format(date);
        } catch (ParseException e) {
            Log.d(LOG_TAG, "Date parsing error");
        }

        return "";
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


}
