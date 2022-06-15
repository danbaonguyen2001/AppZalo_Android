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
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public List<Message> messageList; // lưu trữ danh sách message
    public Context context; // Activity của Adapter
    int ITEM_SEND=1; // Người gửi
    int ITEM_RECIEVE=2; // Người nhận
    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    //Tùy vào View type mà trả về những Holder khác nhau
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
    // Kiểm tra xem message thuộc Type nào từ đó set View tương ứng
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages  = messageList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            // Đối với Sender
            SenderViewHolder viewHolder= (SenderViewHolder) holder;
            if(messages.getType().equals("text")){
                viewHolder.imagemessage.setVisibility(View.GONE);
                viewHolder.textViewmessaage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setText(messages.getMessage());
            }
            else if(messages.getType().equals("image")) {
                viewHolder.imagemessage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setVisibility(View.GONE);
                Glide.with(context).load(messages.getImgUrl())
                        .placeholder(R.drawable.avatar)
                        .into(viewHolder.imagemessage);

            }
            else {
                viewHolder.voicePlayerView.setVisibility(View.VISIBLE);
                viewHolder.voicePlayerView.setAudio(messages.getMessage());
                viewHolder.textViewmessaage.setVisibility(View.GONE);
            }
        }
        else{
            // Đối với Receiver
            RecieverViewHolder viewHolder= (RecieverViewHolder) holder;
            if(messages.getType().equals("text")){
                viewHolder.imagemessage.setVisibility(View.GONE);
                viewHolder.textViewmessaage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setText(messages.getMessage());
            }
            else if(messages.getType().equals("image")) {
                viewHolder.imagemessage.setVisibility(View.VISIBLE);
                viewHolder.textViewmessaage.setVisibility(View.GONE);
                Glide.with(context).load(messages.getImgUrl())
                        .placeholder(R.drawable.avatar)
                        .into(viewHolder.imagemessage);

            }
            else {
                viewHolder.voicePlayerView.setVisibility(View.VISIBLE);
                viewHolder.voicePlayerView.setAudio(messages.getMessage());
                viewHolder.textViewmessaage.setVisibility(View.GONE);
            }
        }

    }
    // Trả về độ lớn của messageList
    @Override
    public int getItemCount() {
        if(messageList!=null){
            return messageList.size();
        }
        return 0;
    }
    // Kiểm tra xem là tin nhắn này đang được nhận hay đang được gửi
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
        // Khai báo các View thuộc layout item sender
        TextView textViewmessaage;
        ImageView imagemessage;
        VoicePlayerView voicePlayerView;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các View
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            imagemessage = itemView.findViewById(R.id.image_message);
            voicePlayerView = itemView.findViewById(R.id.voicePlayerView);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        // Khai báo các View thuộc layout item Receiver
        TextView textViewmessaage;
        ImageView imagemessage;
        VoicePlayerView voicePlayerView;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các View
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            imagemessage = itemView.findViewById(R.id.image_message);
            voicePlayerView = itemView.findViewById(R.id.voicePlayerView);
        }
    }
}
