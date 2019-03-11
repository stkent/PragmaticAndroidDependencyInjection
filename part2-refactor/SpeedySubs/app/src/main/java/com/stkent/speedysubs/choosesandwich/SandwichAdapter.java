package com.stkent.speedysubs.choosesandwich;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stkent.speedysubs.R;
import com.stkent.speedysubs.choosesandwich.ISandwichView.DisplaySandwich;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

final class SandwichAdapter extends ArrayAdapter<DisplaySandwich> {

    SandwichAdapter(@NonNull final Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public View getView(
            final int position,
            @Nullable View rowView,
            @NonNull final ViewGroup parent) {

        final DisplaySandwich sandwich = getItem(position);

        if (rowView == null) {
            rowView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_sandwich, parent, false);

            rowView.setTag(new ViewHolder(rowView));
        }

        final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.nameLabel.setText(sandwich.getName());
        viewHolder.favoriteIndicator.setVisibility(sandwich.isFavorite() ? VISIBLE : GONE);

        return rowView;
    }

    private static class ViewHolder {

        private final TextView nameLabel;
        private final View favoriteIndicator;

        private ViewHolder(final View rowView) {
            this.nameLabel = rowView.findViewById(R.id.name_label);
            this.favoriteIndicator = rowView.findViewById(R.id.favorite_indicator);
        }

    }

}
