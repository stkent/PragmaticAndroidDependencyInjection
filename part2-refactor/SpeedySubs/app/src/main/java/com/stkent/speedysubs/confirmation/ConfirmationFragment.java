package com.stkent.speedysubs.confirmation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stkent.speedysubs.R;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public final class ConfirmationFragment extends Fragment {

    private static final String ARG_ORDER_ID = "order_id";

    @NonNull
    public static ConfirmationFragment newInstance(@NonNull final Integer orderId) {
        final ConfirmationFragment result = new ConfirmationFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ORDER_ID, orderId);
        result.setArguments(args);
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {

        getActivity().setTitle("Order Confirmation");

        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final int orderId = getArguments().getInt(ARG_ORDER_ID);
        ((TextView) view.findViewById(R.id.confirmation_label))
                .setText(String.format(getString(R.string.order_placed_format_string), orderId));

        view.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

}
