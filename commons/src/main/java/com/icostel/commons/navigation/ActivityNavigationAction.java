package com.icostel.commons.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityNavigationAction extends NavigationAction {

    private int screen;
    private Bundle extras;
    private Integer flags;
    private Bundle transition;
    private ScreenProvider provider;

    private ActivityNavigationAction() {
    }

    @Override
    public void navigate(Navigator navigator) {
        // Check if we actually need to just finish
        if (screen == ScreenProvider.FINISH) {
            if (extras != null) {
                Intent data = new Intent();
                data.putExtras(extras);
                navigator.setResult(Activity.RESULT_OK, data);
            } else if (flags != null) {
                navigator.setResult(flags, null);
            }
            navigator.finish();
            return;
        }
        // Otherwise navigate to the target screen
        Intent intent = new Intent(navigator.getContext(), provider.getScreen(screen));
        if (flags != null) {
            intent.setFlags(flags);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (requestCode == null) {
            if (transition != null && navigator instanceof Activity) {
                navigator.startActivity(intent, transition);
            } else {
                navigator.startActivity(intent);
            }
        } else {
            navigator.startActivityForResult(intent, requestCode);
        }
        finishIfNeeded(navigator);
    }

    public static class Builder {
        private ActivityNavigationAction action = new ActivityNavigationAction();

        public Builder setScreenProvider(ScreenProvider screenProvider) {
            action.provider = screenProvider;
            return this;
        }

        public Builder setScreen(int screen) {
            action.screen = screen;
            return this;
        }

        public Builder setBundle(Bundle bundle) {
            action.extras = bundle;
            return this;
        }

        public Builder setTransitionBundle(Bundle bundle) {
            action.transition = bundle;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            action.requestCode = requestCode;
            return this;
        }

        public Builder setShouldFinish(boolean shouldFinish) {
            action.shouldFinish = shouldFinish;
            return this;
        }

        public Builder setFlags(Integer flags) {
            action.flags = flags;
            return this;
        }

        public ActivityNavigationAction build() {
            return action;
        }
    }
}
