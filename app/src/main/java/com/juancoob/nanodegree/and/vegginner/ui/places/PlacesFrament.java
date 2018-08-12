package com.juancoob.nanodegree.and.vegginner.ui.places;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.places.Place;
import com.juancoob.nanodegree.and.vegginner.data.places.PlaceRepository;
import com.juancoob.nanodegree.and.vegginner.util.CheckInternetConnection;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.PlacesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * This fragment shows a map, an spinner to select the place type to find and a list to read easily all results
 * <p>
 * Created by Juan Antonio Cobos Obrero on 6/08/18.
 */
public class PlacesFrament extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ISelectedPlaceCallback {

    @BindView(R.id.tv_map_spinner_label)
    public TextView mapSpinnerLabelTextView;

    @BindView(R.id.acs_place_types)
    public AppCompatSpinner placeTypesAppCompatSpinner;

    @BindView(R.id.rv_places)
    public RecyclerView placesRecyclerView;

    @BindView(R.id.pb_loading_places)
    public ProgressBar loadingPlacesProgressBar;

    @BindView(R.id.iv_powered_by_google)
    public ImageView poweredByGoogleImageView;

    @BindView(R.id.tv_no_places)
    public TextView noPlacesTextView;

    @BindView(R.id.btn_retry)
    public Button retryButton;

    @BindView(R.id.btn_location)
    public Button locationButton;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    @Inject
    public PlaceRepository mPlaceRepository;

    private PlacesViewModel mPlacesViewModel;
    private Context mCtx;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesListAdapter mPlacesListAdapter;
    private List<Marker> mMarkersRetrieved;
    private Parcelable mCurrentPlacesRecyclerViewState;
    private boolean mIsNotTheFirstTime;

    public PlacesFrament() {
        // Required empty public constructor
    }

    public static PlacesFrament getInstance() {
        return new PlacesFrament();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) mCtx.getApplicationContext()).getVegginnerRoomComponent().injectPlacesSection(this);
        mMarkersRetrieved = new ArrayList<>();
        // This boolean helps to check when the system set the location type automatically for the first time
        mIsNotTheFirstTime = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, view);
        requestAccessFineLocationPermission();
        mPlacesViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), vegginnerViewModelFactory).get(PlacesViewModel.class);
        return view;
    }

    private void requestAccessFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(mCtx, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle(R.string.location_dialog_title)
                        .setMessage(R.string.location_dialog_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, Constants.REQUEST_ACCESS_FINE_LOCATION);
                        })
                        .show();
            } else {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, Constants.REQUEST_ACCESS_FINE_LOCATION);
            }

        } else {
            initMaps();
            updateUi();
            initLocation();
            initPlacesRecyclerView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMaps();
                    updateUi();
                    initLocation();
                    initPlacesRecyclerView();
                }
                break;
        }

    }

    private void initMaps() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.f_map);
        supportMapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(mCtx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        mGoogleMap = googleMap;
    }

    private void updateUi() {
        mapSpinnerLabelTextView.setVisibility(View.VISIBLE);
        placeTypesAppCompatSpinner.setVisibility(View.VISIBLE);
        placesRecyclerView.setVisibility(View.VISIBLE);
        showProgressBar();
        poweredByGoogleImageView.setVisibility(View.VISIBLE);
        locationButton.setVisibility(View.GONE);
    }

    private void initLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mCtx);

        mLocationRequest = LocationRequest.create()
                .setInterval(Constants.UPDATE_LOCATION_INTERVAL)
                .setFastestInterval(Constants.FASTEST_UPDATE_LOCATION_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (mCurrentLocation == null || (locationResult.getLastLocation().getLatitude() != mCurrentLocation.getLatitude() || locationResult.getLastLocation().getLongitude() != mCurrentLocation.getLongitude())) {
                    setCurrentLocation(locationResult.getLastLocation());
                    handleCurrentLocation();
                    loadResultsBySelectedPlaceType();
                }
            }
        };
    }

    private void initPlacesRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(mCtx);
        placesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPlacesListAdapter = new PlacesListAdapter(mCtx, this);
        placesRecyclerView.setAdapter(mPlacesListAdapter);
    }

    private void setCurrentLocation(Location newLocation) {
        if (mCurrentLocation == null || (newLocation.getLatitude() != mCurrentLocation.getLatitude() || newLocation.getLongitude() != mCurrentLocation.getLongitude())) {
            mCurrentLocation = newLocation;
            mPlacesViewModel.setLocation(String.format(getString(R.string.location_format),
                    Double.toString(mCurrentLocation.getLatitude()), Double.toString(mCurrentLocation.getLongitude())));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(mCtx, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @OnClick(R.id.btn_location)
    public void onLocationButtonClick() {
        requestAccessFineLocationPermission();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.i("Location services connected");
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
            if (mCurrentLocation != null) {
                mGoogleMap.clear();
                mMarkersRetrieved.clear();
            }
            if (location != null) {
                setCurrentLocation(location);
                handleCurrentLocation();
                loadResultsBySelectedPlaceType();
            } else {
                checkGps();
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        });
    }

    public void checkGps() {
        LocationManager locationManager = (LocationManager) mCtx.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mCtx, R.string.enable_gps, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        handleCurrentLocation();
        loadResultsBySelectedPlaceType();
    }

    @OnItemSelected(R.id.acs_place_types)
    public void onPlaceTypeSelected() {
        // Get selected item and set it to the repository
        String selectedPlace = placeTypesAppCompatSpinner.getSelectedItem().toString();
        String selectedPlaceWithoutSpaces = selectedPlace.contains(" ") ?
                selectedPlace.replaceAll(" ", "_") : selectedPlace;
        mPlaceRepository.setPlaceType(selectedPlaceWithoutSpaces);
        // Don't clear the map if it is the first time because the current location is loaded
        if (mIsNotTheFirstTime) {
            if (mCurrentLocation != null) {
                if (CheckInternetConnection.isConnected(mCtx)) {
                    mGoogleMap.clear();
                    mMarkersRetrieved.clear();
                }
                hideNoElements();
                handleCurrentLocation();
                mPlacesViewModel.loadPlaceData();
                loadResultsBySelectedPlaceType();
            } else {
                checkGps();
            }
        }

        mIsNotTheFirstTime = true;
    }

    public void handleCurrentLocation() {
        Timber.i("Location: %s", mCurrentLocation.toString());
        double latitude = mCurrentLocation.getLatitude();
        double longitude = mCurrentLocation.getLongitude();
        LatLng myLocation = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(myLocation).title(getString(R.string.here));
        Marker marker = mGoogleMap.addMarker(markerOptions);
        // If it is the first time, make the location animation
        if (mMarkersRetrieved.isEmpty()) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));
            Timber.d("Locate me");
        }
        marker.showInfoWindow();
    }

    private void loadResultsBySelectedPlaceType() {
        hideProgressBar();
        mPlacesViewModel.getPlacePagedList().observe(Objects.requireNonNull(getActivity()), places ->
                mPlacesViewModel.getIsReady().observe(Objects.requireNonNull(getActivity()), isReady -> {
                    if (isReady != null && isReady && places != null) {
                        mPlacesListAdapter.submitList(places);
                        if (mCurrentPlacesRecyclerViewState != null) {
                            mLinearLayoutManager.onRestoreInstanceState(mCurrentPlacesRecyclerViewState);
                            mCurrentPlacesRecyclerViewState = null;
                        }
                        checkPlaces(places);
                    }
                })
        );
        mPlacesViewModel.getInitialLoading().observe(Objects.requireNonNull(getActivity()), initialNetworkState ->
                mPlacesListAdapter.checkInitialLoading(initialNetworkState));
        mPlacesViewModel.getNetworkState().observe(getActivity(), networkState ->
                mPlacesListAdapter.setNetworkState(networkState));
    }

    private void checkPlaces(PagedList<Place> places) {
        if (places != null && places.size() > 0) {
            showVegFriendlyPlaces(places);
            hideNoElements();
        } else {
            showNoElements();
        }
    }

    private void showVegFriendlyPlaces(PagedList<Place> places) {
        LatLng newLocation;
        MarkerOptions markerOptions;
        Marker marker;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place place : places) {
            newLocation = new LatLng(place.getGeometry().getLocation().getLatitude(), place.getGeometry().getLocation().getLongitude());
            markerOptions = new MarkerOptions().position(newLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(place.getPlaceName());
            marker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(newLocation));
            mMarkersRetrieved.add(marker);
            builder.include(marker.getPosition());
        }
        LatLngBounds latLngBounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, mCtx.getResources().getInteger(R.integer.zero));
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.i("Location services failed!");

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), Constants.REQUEST_CONNECTION_FAILURE);
            } catch (IntentSender.SendIntentException exception) {
                Timber.e(exception);
            }
        } else {
            Timber.i("Location services connection failed with code: %d", connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.i("Location services suspended");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.SPINNER_POSITION, placeTypesAppCompatSpinner.getSelectedItemPosition());
        outState.putParcelable(Constants.LIST_POSITION, mLinearLayoutManager.onSaveInstanceState());
        outState.putParcelable(Constants.CURRENT_LOCATION, mCurrentLocation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlacesViewModel.getPlacePagedList().removeObservers(Objects.requireNonNull(getActivity()));
        mPlacesViewModel.getIsReady().removeObservers(getActivity());
        mPlacesViewModel.getInitialLoading().removeObservers(getActivity());
        mPlacesViewModel.getNetworkState().removeObservers(getActivity());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            placeTypesAppCompatSpinner.setSelection(savedInstanceState.getInt(Constants.SPINNER_POSITION));
            mCurrentPlacesRecyclerViewState = savedInstanceState.getParcelable(Constants.LIST_POSITION);
            mCurrentLocation = savedInstanceState.getParcelable(Constants.CURRENT_LOCATION);
        }
    }

    @Override
    public void showSelectedPlaceOnMap(Place place) {
        Marker marker = getMarker(place.getPlaceName(), place.getGeometry().getLocation().getLatitude(), place.getGeometry().getLocation().getLongitude());
        if (marker != null) {
            marker.showInfoWindow();
            LatLng latLng = new LatLng(place.getGeometry().getLocation().getLatitude(), place.getGeometry().getLocation().getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Nullable
    private Marker getMarker(String placeName, double placeLatitude, double placeLongitude) {
        for (Marker marker : mMarkersRetrieved) {
            if (marker.getTitle().equals(placeName)
                    && marker.getPosition().latitude == placeLatitude
                    && marker.getPosition().longitude == placeLongitude) {
                return marker;
            }
        }
        return null;
    }

    @Override
    public void showProgressBar() {
        loadingPlacesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loadingPlacesProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadPlacesAgain() {
        if (CheckInternetConnection.isConnected(mCtx)) {
            mGoogleMap.clear();
            mMarkersRetrieved.clear();
            handleCurrentLocation();
        }
        mPlacesViewModel.loadPlaceData();
        loadResultsBySelectedPlaceType();
    }

    @Override
    public void showNoElements() {
        noPlacesTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        placesRecyclerView.setVisibility(View.GONE);
        poweredByGoogleImageView.setVisibility(View.GONE);
    }

    public void hideNoElements() {
        noPlacesTextView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        placesRecyclerView.setVisibility(View.VISIBLE);
        poweredByGoogleImageView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retry)
    public void onRetryButtonClick() {
        hideNoElements();
        showProgressBar();
        loadPlacesAgain();
    }
}
