package com.icostel.arhitecturesample.ui.listusers

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.modules.GlideApp
import com.icostel.arhitecturesample.view.model.User
import com.icostel.commons.utils.AnimationFactory
import com.icostel.commons.utils.bind
import com.icostel.commons.utils.livedata.SingleLiveEvent
import timber.log.Timber
import java.util.*

class UserAdapter(private val context: Context) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = ArrayList<User>()
    internal var selectedUserLive = SingleLiveEvent<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val charityItemView = LayoutInflater.from(this.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(charityItemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindUserViewHolder(users[position])
    }

    fun updateUserList(newUserList: List<User>) {
        Timber.d("updateUserList(), size: %d", newUserList.size)
        val diffCallback = UserDiffCallback(users, newUserList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        users.clear()
        users.addAll(newUserList)
        notifyDataSetChanged()
        // push only the new items after the diff is done
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var rootView: View = itemView.bind(R.id.root_view)
        private var firstName: TextView = itemView.bind(R.id.first_name)
        private var userImage: ImageView = itemView.bind(R.id.user_image)
        private var age: TextView = itemView.bind(R.id.age)

        private val transitionOptions: ActivityOptions
            get() = ActivityOptions.makeSceneTransitionAnimation(itemView.context as Activity, userImage, "user_image_transition")

        fun bindUserViewHolder(user: User) {
            firstName.text = user.firstName
            age.text = context.getString(R.string.age, user.age)

            if (TextUtils.isEmpty(user.resourceUrl)) {
                Timber.d("no user image available")
            } else {
                GlideApp.with(context)
                        .load(user.resourceUrl)
                        .transition(GenericTransitionOptions.with<Drawable>(AnimationFactory.getTransition(AnimationFactory.AnimationType.FADE_IN)))
                        .dontTransform()
                        .into(userImage)
            }

            rootView.setOnClickListener {
                user.transitionBundle = transitionOptions.toBundle()
                selectedUserLive.postValue(user)
            }
        }
    }
}
