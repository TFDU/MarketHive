package com.flagcamp.secondhands.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.model.MapSearchResponse;
import com.flagcamp.secondhands.repository.ProductRepository;

public class MapViewModel extends ViewModel {
    private final ProductRepository repository;
    private final MutableLiveData<Double> latInput = new MutableLiveData<>();
    private final MutableLiveData<Double> lonInput = new MutableLiveData<>();
    private String id_token;

    public void setLat(double lat){
        latInput.setValue(lat);
    }
    public void setLon(double lon){
        lonInput.setValue(lon);
    }
    public void setToken(String id_token){
        this.id_token = id_token;
    }
    public MapViewModel(ProductRepository repository) {
        this.repository = repository;
    }

//TODO: update id_token
    public LiveData<MapSearchResponse> getProductGeoInfo() {
       CustomLiveData input = new CustomLiveData(id_token,latInput,lonInput);
       return Transformations.switchMap(input,repository::getAllProductInfo);
    }
    public static class CustomLiveData extends MediatorLiveData<Cell> {
        public CustomLiveData(String id_token,
                              MutableLiveData<Double> latInput,
                              MutableLiveData<Double> lonInput)
                             // double lat,
                             // double lon) {
        {
            addSource(latInput,new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    setValue(new Cell(id_token, latInput.getValue(),lonInput.getValue()));
                }



            });
        }


    }

    public static class Cell {
        public String id_token;
        public Double lat;
        public Double lon;
        public Cell(String id_token,double lat, double lon) {
            this.id_token = id_token;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
