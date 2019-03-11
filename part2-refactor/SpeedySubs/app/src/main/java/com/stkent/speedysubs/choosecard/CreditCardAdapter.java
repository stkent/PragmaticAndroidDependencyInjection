package com.stkent.speedysubs.choosecard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.networking.ordering.models.CreditCard;

import org.threeten.bp.format.DateTimeFormatter;

final class CreditCardAdapter extends ArrayAdapter<CreditCard> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

    CreditCardAdapter(@NonNull final Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public View getView(
            final int position,
            @Nullable View rowView,
            @NonNull final ViewGroup parent) {

        final CreditCard creditCard = getItem(position);

        if (rowView == null) {
            rowView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_credit_card, parent, false);

            rowView.setTag(new ViewHolder(rowView));
        }

        final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.nameLabel.setText(creditCard.toString());

        final String formattedDate = formatter.format(creditCard.getExpirationDate());
        viewHolder.expirationLabel.setText(
                String.format(
                        getContext().getString(R.string.expires_format_string),
                        formattedDate));

        return rowView;
    }

    private static class ViewHolder {

        private final TextView nameLabel;
        private final TextView expirationLabel;

        private ViewHolder(final View rowView) {
            this.nameLabel = rowView.findViewById(R.id.name_label);
            this.expirationLabel = rowView.findViewById(R.id.expiration_label);
        }

    }

}
