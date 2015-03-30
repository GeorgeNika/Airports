package ua.george_nika.airports.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.george_nika.airports.R;
import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;


public class AirportAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 15;

    private final Context localContext;
    private List<Airport> resultAirports;

    public AirportAutoCompleteAdapter(Context localContext) {
        this.localContext = localContext;
        resultAirports = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return resultAirports.size();
    }

    @Override
    public Airport getItem(int position) {
        return resultAirports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(localContext);
            convertView = inflater.inflate(R.layout.item_airports_list, parent, false);
        }
        Airport airport = getItem(position);
        ((TextView) convertView.findViewById(R.id.text_iata_code)).setText(airport.getIata_code());
        ((TextView) convertView.findViewById(R.id.text_name_eng)).setText(airport.getName_eng());
        ((TextView) convertView.findViewById(R.id.text_city_eng)).setText(airport.getCity_eng());

        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Airport> airports = findAirports(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = airports;
                    filterResults.count = airports.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultAirports = (List<Airport>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};

        return filter;
    }


    private List<Airport> findAirports(String partAirportName) {
        return FactoryDb.getInstance().getAirportsDb().getSearchedAirports(partAirportName);
    }




}
