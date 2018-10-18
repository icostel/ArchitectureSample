package com.icostel.arhitecturesample.navigation;

import android.content.Intent;
import android.os.Bundle;

import com.icostel.arhitecturesample.ui.MainActivity;

public class ActivityNavigationAction extends NavigationAction {
    public enum Screen {
        Finish(null),
        Main(MainActivity.class);

        private Class targetClass;

        Screen(Class targetClass) {
            this.targetClass = targetClass;
        }

        private Class getTargetClass() {
            return targetClass;
        }
    }

    private Screen screen;
    private Bundle extras;
    private Integer flags;

    @Override
    public void navigate(Navigator navigator) {
        // Check if we actually need to just finish
        if (screen == Screen.Finish) {
            navigator.finish();
            return;
        }
        // Otherwise navigate to the target screen
        Intent intent = new Intent(navigator.getContext(), screen.getTargetClass());
        if (flags != null) {
            intent.setFlags(flags);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (requestCode == null) {
            navigator.startActivity(intent);
        } else {
            navigator.startActivityForResult(intent, requestCode);
        }
        finishIfNeeded(navigator);
    }

    public static class Builder {
        private ActivityNavigationAction action = new ActivityNavigationAction();

        public Builder setScreen(Screen screen) {
            action.screen = screen;
            return this;
        }

        public Builder setBundle(Bundle bundle) {
            action.extras = bundle;
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
