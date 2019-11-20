package dell.example.jbplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import dell.example.jbplayer.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private static ClickListener clickListener;
    Context context;
    String [] item;

    public RecyclerViewAdapter(Context context, String[] item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemList.setText(item[position]);
    }

    @Override
    public int getItemCount() {
        return item.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView itemList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemList = itemView.findViewById(R.id.MySongListId);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClickListener(getAdapterPosition(),view);

        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClickListener(getAdapterPosition(),view);
            return false;
        }
    }
    public interface ClickListener{
        void onItemClickListener(int position, View v);
        void onItemLongClickListener(int position,View v);
    }
    public void setItemClickListener(ClickListener clickListener){
        RecyclerViewAdapter.clickListener = clickListener;
    }
}
