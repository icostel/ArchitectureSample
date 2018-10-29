package com.icostel.arhitecturesample.ui.listusers;

import com.icostel.arhitecturesample.model.User;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Handling data updates for users
 */
public class UserDiffCallback extends DiffUtil.Callback {

    private final List<User> oldUserList;
    private final List<User> newuserList;

    UserDiffCallback(List<User> oldUserList, List<User> newUserList) {
        this.oldUserList = oldUserList;
        this.newuserList = newUserList;
    }

    @Override
    public int getOldListSize() {
        return oldUserList.size();
    }

    @Override
    public int getNewListSize() {
        return newuserList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUserList.get(oldItemPosition).getId().equals(newuserList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUserList.get(oldItemPosition).equals(newuserList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
