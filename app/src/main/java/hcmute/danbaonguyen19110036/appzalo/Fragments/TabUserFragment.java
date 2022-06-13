package hcmute.danbaonguyen19110036.appzalo.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import hcmute.danbaonguyen19110036.appzalo.Activities.HomeActivity;
import hcmute.danbaonguyen19110036.appzalo.Activities.RegisterInputInforActivity;
import hcmute.danbaonguyen19110036.appzalo.R;
import hcmute.danbaonguyen19110036.appzalo.Utils.Util;

public class TabUserFragment extends Fragment {
    public ImageView avatar;
    public TextView username;
    public ConstraintLayout cstUser;
    public ImageView btnLogout;
    public TabUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_user, container, false);
        initData(view);
        Picasso.get().load(Util.currentUser.getImg()).into(avatar);
        username.setText(Util.currentUser.getUserName());
        cstUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RegisterInputInforActivity.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(Gravity.CENTER);
            }
        });
        return view;
    }
    public void initData(View view){
        avatar=view.findViewById(R.id.avatar_tab_user);
        username = view.findViewById(R.id.username_tab_user);
        cstUser = view.findViewById(R.id.container_username_tab_user);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
    public void openDialog(int gravity){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_logout);
        Window window = dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        if(Gravity.CENTER==gravity){
            dialog.setCancelable(true);
        }
        else {
            dialog.setCancelable(false);
        }
        TextView textViewCancel =(TextView) dialog.findViewById(R.id.tvCancel);
        TextView textViewLogout = (TextView) dialog.findViewById(R.id.tvLogOut);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });
        dialog.show();
    }
}