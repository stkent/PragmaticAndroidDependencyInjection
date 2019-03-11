package com.stkent.speedysubs.choosesandwich;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.choosecard.CreditCardFragment;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public final class SandwichFragment extends Fragment implements ISandwichView {

    private ProgressBar progressIndicator;
    private SandwichAdapter adapter;
    private SandwichPresenter presenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SandwichPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_choose_sandwich, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        progressIndicator = view.findViewById(R.id.progress_indicator);
        final ListView sandwichListView = view.findViewById(R.id.sandwich_list_view);

        adapter = new SandwichAdapter(getContext());
        sandwichListView.setAdapter(adapter);
        sandwichListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    final AdapterView<?> parent,
                    final View view,
                    final int position,
                    final long id) {

                DisplaySandwich displaySandwich =
                        (DisplaySandwich) parent.getItemAtPosition(position);

                presenter.onSandwichSelected(displaySandwich.getSandwich());
            }
        });

        presenter.onViewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void displaySandwiches(@NonNull final List<DisplaySandwich> sandwiches) {
        adapter.clear();
        adapter.addAll(sandwiches);
    }

    @Override
    public void goToChooseCreditCardScreen() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new CreditCardFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void hideProgressViews() {
        progressIndicator.setVisibility(GONE);
    }

    @Override
    public void setScreenTitle(@NonNull final String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void showError(@NonNull final String errorMessage) {
        Toast.makeText(getContext(), errorMessage, LENGTH_SHORT).show();
    }

    @Override
    public void showProgressViews() {
        progressIndicator.setVisibility(VISIBLE);
    }

}
