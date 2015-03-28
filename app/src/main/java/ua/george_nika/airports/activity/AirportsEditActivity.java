package ua.george_nika.airports.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import ua.george_nika.airports.data.Airport;
import ua.george_nika.airports.database.FactoryDb;
import ua.george_nika.airports.R;

public class AirportsEditActivity extends Activity {

    private CursorAdapter airportsAdapter;
    private ListView listAirports;
    private SearchView searchAirportName;
    private SearchView searchAirportCity;
    private SearchView searchAirportCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airports_edit);

        initializeVariables();
        setAdapters();
        setListeners();
    }

    private void initializeVariables(){
        listAirports = (ListView)findViewById(R.id.list_airports);
        searchAirportName = (SearchView)findViewById(R.id.search_name);
        searchAirportCity = (SearchView)findViewById(R.id.search_city);
        searchAirportCountry = (SearchView)findViewById(R.id.search_country);
    }

    private void setAdapters(){
        airportsAdapter = new SimpleCursorAdapter( this,
                R.layout.item_airports_list,
                FactoryDb.getInstance().getAirportsDb().getCursorWithSearchedStrings(
                        searchAirportName.getQuery().toString(),
                        searchAirportCity.getQuery().toString(),
                        searchAirportCountry.getQuery().toString()
                ),
                FactoryDb.getInstance().getAirportsDb().getFieldsNameForCursorAdapter(),
                new int[] {R.id.text_iata_code, R.id.text_name_eng, R.id.text_city_eng},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listAirports.setAdapter(airportsAdapter);
    }

    private void setListeners(){
        ImageButton addButton = (ImageButton)findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AirportAddDialog(v.getContext(),null,null).showAirportDialog();
            }
        });

        listAirports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Integer airportId = cursor.getInt(cursor.getColumnIndex("_id"));
                Airport airport = FactoryDb.getInstance().getAirportsDb().getAirportFromId(airportId);
                new AirportInfoDialog(view.getContext(),airportId,airport).showAirportDialog();
            }
        });

        SearchView.OnQueryTextListener commonListener = new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAirportAction();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAirportAction();
                return true;
            }
        };

        searchAirportName.setOnQueryTextListener(commonListener);
        searchAirportCity.setOnQueryTextListener(commonListener);
        searchAirportCountry.setOnQueryTextListener(commonListener);
    }

    private void searchAirportAction(){
        setAdapters();
    }


    //******************* Class ************************


    private abstract class AirportDialog extends AlertDialog{
        protected Airport airport;
        protected Integer _id;
        protected Boolean editPermission;
        protected AirportDialog.Builder builder;
        Context context;

        protected AirportDialog(Context context, Integer _id, Airport airport) {
            super(context);
            this.context = context;
            this._id = _id;
            this.airport = airport;
            builder = new AirportDialog.Builder(context);
        }

        protected abstract void setTitleAndPermission();
        protected abstract void setButtons();


        public void showAirportDialog(){
            setTitleAndPermission();
            setButtons();

            View view = View.inflate(context, R.layout.dialog_airport_data, null);
            setListenersForJumpButton(view);
            builder.setView(view);

            if (airport!=null) {
                setAirportDataInDialog(view, airport, editPermission);
            }

            AlertDialog dialog = builder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        private void setListenersForJumpButton(View view){
            Button jumpToWebSite = (Button)view.findViewById(R.id.button_web);
            jumpToWebSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AirportsEditActivity.this,BrowseWebSiteActivity.class);
                    intent.putExtra(BrowseWebSiteActivity.EXTRA_WEB_SITE,airport.getWebsite());
                    startActivity(intent);
                }
            });
            Button jumpToGoogleMap = (Button)view.findViewById(R.id.button_google_map);
            jumpToGoogleMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo goto google map
                    Intent intent = new Intent(AirportsEditActivity.this,GoogleMapActivity.class);
                    intent.putExtra(GoogleMapActivity.EXTRA_LATITUDE,airport.getLatitude());
                    intent.putExtra(GoogleMapActivity.EXTRA_LONGITUDE,airport.getLongitude());
                    startActivity(intent);
                }
            });

            Button jumpToYandexMap = (Button)view.findViewById(R.id.button_yandex_map);
            jumpToYandexMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo goto yandex map
                }
            });
        }

        protected void addAirportToDb(DialogInterface dialog){
            AlertDialog ald = (AlertDialog)dialog;
            Airport airport = getAirportDataFromDialog(ald);
            FactoryDb.getInstance().getAirportsDb().addAirport(airport);
            setAdapters();
        }
        protected void editAirportInDb(DialogInterface dialog){
            AlertDialog ald = (AlertDialog)dialog;
            Airport airport = getAirportDataFromDialog(ald);
            FactoryDb.getInstance().getAirportsDb().editAirport(_id,airport);
            setAdapters();
        }
        protected void deleteAirportFromDb(DialogInterface dialog){
            FactoryDb.getInstance().getAirportsDb().deleteAirport(_id);
            setAdapters();
        }

        protected void setAirportDataInDialog(View view, Airport airport, Boolean editPermission) {
            setDataOnEditTextInAirportDialog(view,R.id.edit_iata_code,airport.getIata_code(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_icao_code,airport.getIcao_code(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_name_rus,airport.getName_rus(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_name_eng,airport.getName_eng(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_city_rus,airport.getCity_rus(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_city_eng,airport.getCity_eng(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_gmt_offset,""+airport.getGmt_offset(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_country_rus,airport.getCountry_rus(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_country_eng,airport.getCountry_eng(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_iso_code,airport.getIso_code(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_latitude,""+airport.getLatitude(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_longtitude,""+airport.getLongitude(),editPermission);
            setDataOnEditTextInAirportDialog(view,R.id.edit_website,airport.getWebsite(),editPermission);
        }

        private void setDataOnEditTextInAirportDialog(View view, int resourceId, String data, Boolean editPermission){
            EditText tempEditText = (EditText)view.findViewById(resourceId);
            tempEditText.setText(data);
            tempEditText.setEnabled(editPermission);
        }

        private String getDataFromEditTextInAirportDialog(AlertDialog view, int resourceId){
            EditText tempEditText = (EditText)view.findViewById(resourceId);
            return tempEditText.getText().toString();
        }
        protected Airport getAirportDataFromDialog(AlertDialog view) {
            Airport resultAirport = new Airport();
            resultAirport.setIata_code(getDataFromEditTextInAirportDialog(view,R.id.edit_iata_code));
            resultAirport.setIcao_code(getDataFromEditTextInAirportDialog(view, R.id.edit_icao_code));
            resultAirport.setName_rus(getDataFromEditTextInAirportDialog(view, R.id.edit_name_rus));
            resultAirport.setName_eng(getDataFromEditTextInAirportDialog(view, R.id.edit_name_eng));
            resultAirport.setCity_rus(getDataFromEditTextInAirportDialog(view, R.id.edit_city_rus));
            resultAirport.setCity_eng(getDataFromEditTextInAirportDialog(view, R.id.edit_city_eng));
            resultAirport.setGmt_offset(Float.parseFloat(getDataFromEditTextInAirportDialog(view, R.id.edit_gmt_offset)));
            resultAirport.setCountry_rus(getDataFromEditTextInAirportDialog(view, R.id.edit_country_rus));
            resultAirport.setCountry_eng(getDataFromEditTextInAirportDialog(view, R.id.edit_country_eng));
            resultAirport.setIso_code(getDataFromEditTextInAirportDialog(view, R.id.edit_iso_code));
            resultAirport.setLatitude(Float.parseFloat(getDataFromEditTextInAirportDialog(view, R.id.edit_latitude)));
            resultAirport.setLongitude(Float.parseFloat(getDataFromEditTextInAirportDialog(view, R.id.edit_longtitude)));
            resultAirport.setWebsite(getDataFromEditTextInAirportDialog(view, R.id.edit_website));

            return  resultAirport;
        }

    }

    private class AirportAddDialog extends AirportDialog{

        protected AirportAddDialog(Context context, Integer _id, Airport airport) {
            super(context, null, null);
        }

        @Override
        protected void setTitleAndPermission() {
            editPermission = true;
            builder.setTitle("ADD Airport");
        }
        @Override
        protected void setButtons() {
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addAirportToDb(dialog);
                }
            });
            builder.setNegativeButton("CANCEL",null);

        }
    }
    private class AirportInfoDialog extends AirportDialog{

        protected AirportInfoDialog(Context context, Integer _id, Airport airport) {
            super(context, _id, airport);
        }

        @Override
        protected void setTitleAndPermission() {
            editPermission = false;
            builder.setTitle("INFO Airport");
        }

        @Override
        protected void setButtons() {
            builder.setPositiveButton("OK", null);
            builder.setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog ald = (AlertDialog)dialog;
                    Airport airport = getAirportDataFromDialog(ald);
                    new AirportEditDialog(context,_id,airport).showAirportDialog();
                }
            });

            builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAirportFromDb(dialog);
                }
            });

        }
    }
    private class AirportEditDialog extends AirportDialog{

        protected AirportEditDialog(Context context, Integer _id, Airport airport) {
            super(context, _id, airport);
        }

        @Override
        protected void setTitleAndPermission() {
            editPermission = true;
            builder.setTitle("EDIT Airport");
        }

        @Override
        protected void setButtons() {
            builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editAirportInDb(dialog);
                }
            });
            builder.setNegativeButton("CANCEL",null);

        }
    }
}
