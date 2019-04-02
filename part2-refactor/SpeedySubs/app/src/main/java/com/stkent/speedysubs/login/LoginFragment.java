package com.stkent.speedysubs.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.choosesandwich.SandwichFragment;
import com.stkent.speedysubs.networking.Callback;
import com.stkent.speedysubs.networking.ordering.OrderingApi;
import com.stkent.speedysubs.networking.ordering.models.Customer;
import com.stkent.speedysubs.state.Session;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public final class LoginFragment extends Fragment implements ILoginView {

    private ProgressBar progressIndicator;
    private EditText usernameField, passwordField;

    private LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this, new OrderingApi(), Session.getSharedInstance());
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        getActivity().setTitle("Login");

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        progressIndicator = view.findViewById(R.id.progress_indicator);
        usernameField = view.findViewById(R.id.username_field);
        passwordField = view.findViewById(R.id.password_field);
        final Button submitButton = view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                presenter.onSubmitTapped(
                        usernameField.getText().toString(),
                        passwordField.getText().toString());
            }
        });
    }

    @Override
    public void showProgressIndicators(final boolean show) {
        progressIndicator.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void goToChooseSandwichScreen() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SandwichFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void displayError(@NonNull final String message) {
        Toast.makeText(getContext(), message, LENGTH_SHORT).show();
    }

}
