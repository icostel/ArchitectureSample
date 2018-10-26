package com.icostel.arhitecturesample.ui.listusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.modules.GlideApp;
import com.icostel.arhitecturesample.model.User;
import com.icostel.arhitecturesample.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHodler> {

    private final List<User> users = new ArrayList<>();
    private Context context;
    private SingleLiveEvent<User> selectedUserLive = new SingleLiveEvent<>();

    UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View charityItemView = LayoutInflater.from(this.context).inflate(R.layout.item_user, parent, false);
        return new UserViewHodler(charityItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHodler holder, int position) {
        User user = users.get(position);
        if (user != null) {
            holder.bindUserViewHolder(user);
        }
    }

    void updateUserList(List<User> newItems) {
        //final UserDiffCallback diffCallback = new UserDiffCallback(newItems, users);
        //final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.users.clear();
        this.users.addAll(newItems);
        notifyDataSetChanged();
        //diffResult.dispatchUpdatesTo(this);
    }

    SingleLiveEvent<User> getSelectedUserLive() {
        return selectedUserLive;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view)
        View rootView;

        @BindView(R.id.first_name)
        TextView firstName;

        @BindView(R.id.image)
        ImageView userImage;

        @BindView(R.id.age)
        TextView age;

        UserViewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindUserViewHolder(User user) {
            firstName.setText(user.getFirstName());
            age.setText(context.getString(R.string.age, String.valueOf(user.getAge())));
            GlideApp.with(context)
                    .load(user.getResourceUrl())
                    .centerInside()
                    .into(userImage);
            rootView.setOnClickListener(v -> selectedUserLive.postValue(user));
        }
    }
}
