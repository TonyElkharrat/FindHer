package com.example.findher.RecyclerViewChat;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.findher.R;
import com.example.findher.models.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageViewHolder>
{

    public interface Listener
    {
        void onDataChanged();
    }

    private Listener callback;
    //FOR DATA
    private  RequestManager glide  ;
    private final String idCurrentUser;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide,Listener callback, String idCurrentUser) {
        super(options);
        this.idCurrentUser = idCurrentUser;
        this.glide = glide;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_mentor_chat_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i, @NonNull Message message)
    {
        messageViewHolder.updateWithMessage(message, this.idCurrentUser, this.glide);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout rootView;
        private LinearLayout profileContainer;
        ImageView imageViewProfile;
        ImageView imageViewIsMentor;
        CardView cardViewImageSent;
        ImageView imageViewSent;
        TextView textViewMessage;
        TextView textViewDate;
        LinearLayout textMessageContainer;
        RelativeLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rootView = itemView.findViewById(R.id.activity_mentor_chat_item_root_view);

            profileContainer = itemView.findViewById(R.id.activity_mentor_chat_item_profile_container);
            imageViewProfile = itemView.findViewById(R.id.activity_mentor_chat_item_profile_container_profile_image);
            imageViewIsMentor= itemView.findViewById(R.id.activity_mentor_chat_item_profile_container_is_mentor_image);

            messageContainer= itemView.findViewById(R.id.activity_mentor_chat_item_message_container);

            cardViewImageSent= itemView.findViewById(R.id.activity_mentor_chat_item_message_container_image_sent_cardview);

            imageViewSent= itemView.findViewById(R.id.activity_mentor_chat_item_message_container_image_sent_cardview_image);

             textMessageContainer= itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_message_container);
            textViewMessage= itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_message_container_text_view);
            textViewDate = itemView.findViewById(R.id.activity_mentor_chat_item_message_container_text_view_date);

        }

        public void updateWithMessage(Message message, String currentUserId, RequestManager glide){

            // Check if current user is the sender
            Boolean isCurrentUser = message.getUserSender().getuId().equals(currentUserId);

            // Update message TextView
            this.textViewMessage.setText(message.getMessage());
            this.textViewMessage.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

            // Update date TextView
            if (message.getDateCreated() != null) this.textViewDate.setText(this.convertDateToHour(message.getDateCreated()));

            // Update isMentor ImageView

            // Update profile picture ImageView
            if (message.getUserSender().getUrlPicture() != null)
                glide.load(message.getUserSender().getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);

            // Update image sent ImageView
            if (message.getUrlImage() != null){
                glide.load(message.getUrlImage())
                        .into(imageViewSent);
                this.imageViewSent.setVisibility(View.VISIBLE);
            }
            else
                {
                this.imageViewSent.setVisibility(View.GONE);
            }

            //Update Message Bubble Color Background
            ((GradientDrawable) textMessageContainer.getBackground()).setColor(isCurrentUser ? ContextCompat.getColor(itemView.getContext(), R.color.blueClear) : ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));

            // Update all views alignment depending is current user or not
            this.updateDesignDependingUser(isCurrentUser);
        }

        private void updateDesignDependingUser(Boolean isSender)
        {
            // PROFILE CONTAINER
            RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsLayoutHeader.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
            this.profileContainer.setLayoutParams(paramsLayoutHeader);

            // MESSAGE CONTAINER
            RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsLayoutContent.addRule(isSender ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.activity_mentor_chat_item_profile_container);
            this.messageContainer.setLayoutParams(paramsLayoutContent);

            // CARDVIEW IMAGE SEND
            RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsImageView.addRule(isSender ? RelativeLayout.ALIGN_LEFT : RelativeLayout.ALIGN_RIGHT, R.id.activity_mentor_chat_item_message_container_text_message_container);
            this.cardViewImageSent.setLayoutParams(paramsImageView);

            this.rootView.requestLayout();
        }

        private String convertDateToHour(Date date)
         {
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            return dfTime.format(date);
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

}
