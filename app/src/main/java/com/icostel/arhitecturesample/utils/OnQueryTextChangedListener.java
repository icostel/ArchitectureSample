package com.icostel.arhitecturesample.utils;

import androidx.appcompat.widget.SearchView;

public interface OnQueryTextChangedListener extends SearchView.OnQueryTextListener {

    @Override
    default boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    default boolean onQueryTextChange(String newText) {
        textChanged(newText);
        return true;
    }

    void textChanged(String newText);
}
