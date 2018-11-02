package com.icostel.arhitecturesample.ui.listusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.modules.GlideApp;
import com.icostel.arhitecturesample.model.User;
import com.icostel.arhitecturesample.utils.AnimationFactory;
import com.icostel.arhitecturesample.utils.ImageRequestListener;
import com.icostel.arhitecturesample.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private static final String TAG = UserAdapter.class.getCanonicalName();

    private final List<User> users = new ArrayList<>();
    private Context context;
    private SingleLiveEvent<User> selectedUserLive = new SingleLiveEvent<>();

    UserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View charityItemView = LayoutInflater.from(this.context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(charityItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindUserViewHolder(users.get(position));
    }

    void updateUserList(List<User> newUserList) {
        Timber.d("updateUserList(), size: %d", newUserList.size());
        final UserDiffCallback diffCallback = new UserDiffCallback(users, newUserList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        users.clear();
        users.addAll(newUserList);
        notifyDataSetChanged();//TODO remove this
        // push only the new items after the diff is done
        diffResult.dispatchUpdatesTo(this);
    }

    SingleLiveEvent<User> getSelectedUserLive() {
        return selectedUserLive;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view)
        View rootView;

        @BindView(R.id.first_name)
        TextView firstName;

        @BindView(R.id.user_image)
        ImageView userImage;

        @BindView(R.id.age)
        TextView age;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindUserViewHolder(User user) {
            firstName.setText(user.getFirstName());
            age.setText(context.getString(R.string.age, String.valueOf(user.getAge())));
            GlideApp.with(context)
                    .load(user.getResourceUrl())
                    .transition(GenericTransitionOptions.with(AnimationFactory.getTransition(AnimationFactory.AnimationType.FADE_IN)))
                    .dontTransform()
                    .listener((ImageRequestListener) status -> Timber.e("%s, glide listener status: %s", TAG, status.toString()))
                    .into(userImage);
            rootView.setOnClickListener(v -> selectedUserLive.postValue(user));
        }
    }


}
