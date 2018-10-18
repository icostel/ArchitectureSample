package com.icostel.arhitecturesample.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;

public class FragmentNavigationAction extends NavigationAction {
    public interface TargetFragment {
        Class<? extends Fragment> getFragmentClass();
    }

    private TargetFragment targetFragment;
    private Bundle args;
    private boolean addToBackstack;

    @Override
    public void navigate(Navigator navigator) {
        try {
            Fragment fragment = targetFragment.getFragmentClass().newInstance();
            fragment.setArguments(args);
            FragmentTransaction transaction = navigator.getSupportFragmentManager().beginTransaction();
            transaction.replace(navigator.getFragmentContainer(), fragment,
                    Integer.toString(navigator.getSupportFragmentManager().getBackStackEntryCount()));
            if (addToBackstack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            Timber.e(e);
        }
    }

    public static class Builder {
        private FragmentNavigationAction action = new FragmentNavigationAction();

        public Builder setTargetFragment(TargetFragment targetFragment) {
            action.targetFragment = targetFragment;
            return this;
        }

        public Builder setArguments(Bundle arguments) {
            action.args = arguments;
            return this;
        }

        public Builder setAddToBackstack(boolean addToBackstack) {
            action.addToBackstack = addToBackstack;
            return this;
        }

        public Builder setShouldFinish(boolean shouldFinish) {
            action.shouldFinish = shouldFinish;
            return this;
        }

        public FragmentNavigationAction build() {
            return action;
        }
    }
}
