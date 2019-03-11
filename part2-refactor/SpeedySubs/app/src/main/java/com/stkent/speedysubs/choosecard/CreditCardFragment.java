package com.stkent.speedysubs.choosecard;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.confirmation.ConfirmationFragment;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public final class CreditCardFragment extends Fragment {

    private ProgressBar progressIndicator;
    private ListView creditCardListView;
    private SwipeRefreshLayout creditCardRefresher;
    private ArrayAdapter<CreditCard> adapter;
    private CreditCardViewModel viewModel;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders
                .of(this)
                .get(CreditCardViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_choose_credit_card, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        progressIndicator = view.findViewById(R.id.progress_indicator);
        creditCardListView = view.findViewById(R.id.credit_card_list_view);
        creditCardRefresher = view.findViewById(R.id.credit_card_refresh_layout);

        adapter = new CreditCardAdapter(getContext());
        creditCardListView.setAdapter(adapter);

        creditCardRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.onCreditCardsRefreshed();
            }
        });

        bindViewModel();
    }

    private void bindViewModel() {
        viewModel.title().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                getActivity().setTitle(s);
            }
        });

        viewModel.showProgressViews().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean show) {
                progressIndicator.setVisibility(show ? VISIBLE : GONE);
            }
        });

        viewModel.endRefresh().observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object ignored) {
                creditCardRefresher.setRefreshing(false);
            }
        });

        viewModel.creditCards().observe(this, new Observer<List<CreditCard>>() {
            @Override
            public void onChanged(@Nullable final List<CreditCard> creditCards) {
                adapter.clear();
                adapter.addAll(creditCards);
            }
        });

        viewModel.orderConfirmationNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer orderId) {
                viewModel.orderConfirmationNumber().removeObserver(this);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConfirmationFragment.newInstance(orderId))
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        viewModel.errors().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String errorMessage) {
                Toast.makeText(getContext(), errorMessage, LENGTH_SHORT).show();
            }
        });

        creditCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    final AdapterView<?> parent,
                    final View view,
                    final int position,
                    final long id) {

                viewModel.onCreditCardSelected(adapter.getItem(position));
            }
        });
    }

}
