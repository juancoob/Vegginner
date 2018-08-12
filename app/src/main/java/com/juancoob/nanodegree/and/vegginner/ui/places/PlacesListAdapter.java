package com.juancoob.nanodegree.and.vegginner.ui.places;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.places.Place;
import com.juancoob.nanodegree.and.vegginner.util.CheckInternetConnection;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;
import com.juancoob.nanodegree.and.vegginner.util.callbacks.IAlertDialogCallback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter binds data from places API into the view
 * <p>
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class PlacesListAdapter extends PagedListAdapter<Place, RecyclerView.ViewHolder> implements IAlertDialogCallback {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private Context mCtx;
    private NetworkState mNetworkState;
    private ISelectedPlaceCallback mSelectedPlaceCallback;


    protected PlacesListAdapter(Context ctx, ISelectedPlaceCallback selectedPlaceCallback) {
        super(Place.DIFF_CALLBACK);
        mCtx = ctx;
        mSelectedPlaceCallback = selectedPlaceCallback;
    }

    public void setNetworkState(NetworkState networkState) {
        NetworkState previousState = mNetworkState;
        boolean wasLoading = isLoadingData();
        mNetworkState = networkState;
        boolean willLoad = isLoadingData();

        if (wasLoading != willLoad) {
            if (wasLoading) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (willLoad && previousState != networkState) {
            notifyItemChanged(getItemCount());
        }
    }

    public void checkInitialLoading(NetworkState networkState) {
        if (networkState != null && networkState.getState() == NetworkState.Status.FAILED) {
            mSelectedPlaceCallback.hideProgressBar();

            if (CheckInternetConnection.isConnected(mCtx)) {
                CheckInternetConnection.showDialog(PlacesListAdapter.this, mCtx, R.string.something_wrong_title, R.string.something_wrong_message, R.string.retry, R.string.no);
            } else {
                CheckInternetConnection.showDialog(PlacesListAdapter.this, mCtx, R.string.no_internet_title, R.string.no_internet_message, R.string.retry, R.string.no);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingData() && position == getItemCount()) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isLoadingData() {
        return mNetworkState != null && mNetworkState != NetworkState.LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PROGRESS) {
            view = LayoutInflater.from(mCtx).inflate(R.layout.item_load_progress, parent, false);
            return new LoadingViewHolder(view);
        } else {
            view = LayoutInflater.from(mCtx).inflate(R.layout.item_place, parent, false);
            return new PlaceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaceViewHolder) {
            ((PlaceViewHolder) holder).bindTo();
        } else {
            ((LoadingViewHolder) holder).bindView(mNetworkState);
        }
        mSelectedPlaceCallback.hideProgressBar();
    }

    // Alert dialog results
    @Override
    public void showPositiveResult() {
        mSelectedPlaceCallback.showProgressBar();
        mSelectedPlaceCallback.loadPlacesAgain();
    }

    @Override
    public void showNegativeResult() {
        if (getItemCount() == 0) {
            mSelectedPlaceCallback.showNoElements();
        }
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_place_icon)
        public ImageView placeIconImageView;

        @BindView(R.id.tv_place_rating)
        public TextView placeRatingTextView;

        @BindView(R.id.tv_place_name)
        public TextView placeNameTextView;

        @BindView(R.id.tv_place_vicinity)
        public TextView placeVicinityTextView;

        private Place mPlace;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindTo() {
            mPlace = getItem(getAdapterPosition());
            Picasso.get().load(mPlace.getIcon()).into(placeIconImageView);
            placeRatingTextView.setText(String.valueOf(mPlace.getRating()));
            placeNameTextView.setText(mPlace.getPlaceName());
            placeVicinityTextView.setText(mPlace.getVicinity());
        }

        @Override
        public void onClick(View view) {
            mSelectedPlaceCallback.showSelectedPlaceOnMap(getItem(getAdapterPosition()));
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pb_loading)
        public ProgressBar loadingProgressBar;

        @BindView(R.id.tv_no_results)
        public TextView noResultsTextView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(NetworkState networkState) {

            if (networkState != null && networkState.getState() == NetworkState.Status.RUNNING) {
                loadingProgressBar.setVisibility(View.VISIBLE);
            } else {
                loadingProgressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getState() == NetworkState.Status.FAILED) {
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(mCtx.getString(R.string.no_results));
                if (CheckInternetConnection.isConnected(mCtx)) {
                    CheckInternetConnection.showDialog(PlacesListAdapter.this, mCtx, R.string.something_wrong_title, R.string.something_wrong_message, R.string.retry, R.string.no);
                } else {
                    CheckInternetConnection.showDialog(PlacesListAdapter.this, mCtx, R.string.no_internet_title, R.string.no_internet_message, R.string.retry, R.string.no);
                }
            } else {
                noResultsTextView.setVisibility(View.GONE);
            }
        }
    }
}
