package fr.supinternet.slike;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by trump on 26/09/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Feed> messages = new ArrayList<>();

    public FeedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Feed message = messages.get(position);
        holder.tvName.setText(message.getUser() + ":");
        holder.tvMessage.setText(message.getMessage());
    }

    public void addMessage(Feed message){
        Log.i("test", "adding " + message);
        messages.add(0, message);
        notifyDataSetChanged();
    }

    public void removeMessage(Feed message){
        messages.remove(message);
}

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
        }
    }
}
