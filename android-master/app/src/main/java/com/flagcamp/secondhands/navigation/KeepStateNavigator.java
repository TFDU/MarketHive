package com.flagcamp.secondhands.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.Navigator.Name;
import androidx.navigation.fragment.FragmentNavigator;


@Name("keep_state_fragment")
public final class KeepStateNavigator extends FragmentNavigator {
    private final Context context;
    private final FragmentManager manager;
    private final int containerId;

    public KeepStateNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.context = context;
        this.manager = manager;
        this.containerId = containerId;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args,
                                   @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        String tag = String.valueOf(destination.getId());
        FragmentTransaction transaction = this.manager.beginTransaction();
        boolean initialNavigate = false;
        Fragment currentFragment = this.manager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            transaction.detach(currentFragment);
        } else {
            initialNavigate = true;
        }

        Fragment fragment = this.manager.findFragmentByTag(tag);
        if (fragment == null) {
            String className = destination.getClassName();
            fragment = this.manager.getFragmentFactory().instantiate(this.context.getClassLoader(), className);
            transaction.add(this.containerId, fragment, tag);
        } else {
            transaction.attach(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();
        return initialNavigate ? destination : null;
    }

}