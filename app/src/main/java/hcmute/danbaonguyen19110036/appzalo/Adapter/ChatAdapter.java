package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public List<Message> messageList;
    public Context context;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_sender_chat,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view =LayoutInflater.from(context).inflate(R.layout.fragment_receiver_chat,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages  = messageList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder= (SenderViewHolder) holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
        }
        else{
            RecieverViewHolder viewHolder= (RecieverViewHolder) holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if(messageList!=null){
            return messageList.size();
        }
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        Message messages=messageList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIEVE;
        }
    }
    class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessaage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessaage;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
        }
    }
}
