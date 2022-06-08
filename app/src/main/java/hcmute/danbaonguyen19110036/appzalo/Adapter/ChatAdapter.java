package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.List;
import hcmute.danbaonguyen19110036.appzalo.Model.Message;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

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
            if(messages.getType().equals("text")){
                viewHolder.imagemessage.setVisibility(View.GONE);
                viewHolder.textViewmessaage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setText(messages.getMessage());
            }
            else {
                viewHolder.imagemessage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setVisibility(View.GONE);
                Glide.with(context).load(messages.getImgUrl())
                        .placeholder(R.drawable.avatar)
                        .into(viewHolder.imagemessage);

            }
        }
        else{
            RecieverViewHolder viewHolder= (RecieverViewHolder) holder;
            if(messages.getType().equals("text")){
                viewHolder.imagemessage.setVisibility(View.GONE);
                viewHolder.textViewmessaage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setText(messages.getMessage());
            }
            else {
                viewHolder.imagemessage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setVisibility(View.GONE);
                Glide.with(context).load(messages.getImgUrl())
                        .placeholder(R.drawable.avatar)
                        .into(viewHolder.imagemessage);

            }
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
        if(Util.currentUser.getId().equals(messages.getSenderId()))
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
        ImageView imagemessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            imagemessage = itemView.findViewById(R.id.image_message);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessaage;
        ImageView imagemessage;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            imagemessage = itemView.findViewById(R.id.image_message);
        }
    }
}
