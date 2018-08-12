package com.juancoob.nanodegree.and.vegginner.ui.places;

import com.juancoob.nanodegree.and.vegginner.data.places.Place;

/**
 * This callback helps the communication between the place recyclerview and its adapter
 *
 * Created by Juan Antonio Cobos Obrero on 9/08/18.
 */
public interface ISelectedPlaceCallback {
    void showSelectedPlaceOnMap(Place place);
    void showProgressBar();
    void hideProgressBar();
    void loadPlacesAgain();
    void showNoElements();
}
