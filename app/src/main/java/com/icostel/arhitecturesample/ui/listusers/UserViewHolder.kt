package com.icostel.arhitecturesample.ui.listusers

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.modules.GlideApp
import com.icostel.arhitecturesample.view.model.User
import com.icostel.commons.adapter.BaseViewHolder
import com.icostel.commons.utils.AnimatorFactory
import com.icostel.commons.utils.bind
import timber.log.Timber

class UserViewHolder(private val context: Context, itemView: View) : BaseViewHolder<User>(itemView) {

    private val firstName: TextView = itemView.bind(R.id.first_name)
    private val userImage: ImageView = itemView.bind(R.id.user_image)
    private val age: TextView = itemView.bind(R.id.age)
    internal val rootView: View = itemView.bind(R.id.root_view)

    internal val transitionOptions: ActivityOptions
        get() = ActivityOptions.makeSceneTransitionAnimation(itemView.context as Activity,
                userImage, "user_image_transition")

    override fun bind(item: User?) {
        item?.let {
            firstName.text = item.firstName.capitalize()
            age.text = context.getString(R.string.age, item.age)

            if (item.resourceUrl.isEmpty()) {
                Timber.d("no user image available")
            } else {
                GlideApp.with(context)
                        .load(item.resourceUrl)
                        .transition(GenericTransitionOptions.with<Drawable>(ViewPropertyTransition.Animator {
                            if (it != null) {
                                AnimatorFactory.getAnimator(AnimatorFactory.AnimationType.FADE_IN, it)?.start()
                            }
                        }))
                        .dontTransform()
                        .into(userImage)
            }
        }
    }
}