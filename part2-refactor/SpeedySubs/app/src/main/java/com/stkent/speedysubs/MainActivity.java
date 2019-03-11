package com.stkent.speedysubs;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stkent.speedysubs.login.LoginFragment;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        final int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 3) {
            getSupportFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
        } else if (backStackCount > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
