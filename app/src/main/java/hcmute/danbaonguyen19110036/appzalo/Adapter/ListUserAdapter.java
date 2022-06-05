package hcmute.danbaonguyen19110036.appzalo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hcmute.danbaonguyen19110036.appzalo.Model.User;
import hcmute.danbaonguyen19110036.appzalo.R;

public class ListUserAdapter extends BaseAdapter {
    private List<User> userList;
    private Context context;
    private int layout;

    public ListUserAdapter(List<User> userList, Context context, int layout) {
        this.userList = userList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        private TextView emailuser,newMessage;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
//            holder.emailuser= (TextView) view.findViewById(R.id.emailuser);
//            holder.newMessage = (TextView) view.findViewById(R.id.newMessage);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        User user = userList.get(i);
        holder.emailuser.setText(user.getUserName());
        return view;
    }
}
