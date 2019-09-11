package com.icostel.arhitecturesample.ui.userdetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.Injectable
import com.icostel.arhitecturesample.di.modules.GlideApp
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.arhitecturesample.view.model.User
import com.icostel.commons.utils.AnimatorFactory
import com.icostel.commons.utils.bind
import com.icostel.commons.utils.extensions.observe

class UserDetailsFragment : BaseFragment(), Injectable {

    private lateinit var toolbar: Toolbar
    private lateinit var userImage: ImageView

    private var userDetailsViewModel: UserDetailsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userDetailsViewModel = getViewModel(UserDetailsViewModel::class.java)

        val layout = inflater.inflate(R.layout.layout_user_details, container, false)
        toolbar = layout.bind(R.id.toolbar)
        userImage = layout.bind(R.id.user_image)

        enableUpNavigation(true)
        toolbar.setNavigationOnClickListener { activity?.supportFinishAfterTransition() }

        val userDetailsViewModel = getViewModel(UserDetailsViewModel::class.java)
        val userId = arguments?.getString(Config.Data.USER_ID)
        if (TextUtils.isEmpty(userId)) {
            activity?.supportFinishAfterTransition()
        } else {
            userDetailsViewModel.init(userId)
            userDetailsViewModel.userLiveData.observe(this) { bindUserData(it) }
        }

        return layout
    }

    private fun bindUserData(user: User?) {
        if (user != null) {
            GlideApp.with(this)
                    .load(user.resourceUrl)
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
