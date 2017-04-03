package habbitatvalley.com.geebelataxigeebela.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.models.PlacesItem;

public class PlacesAdapter extends BaseAdapter {

    public ArrayList<PlacesItem> places;
    Context context;

    public PlacesAdapter(Context context, ArrayList<PlacesItem> places) {

        this.places = places;
        this.context = context;

    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public PlacesItem getItem(int i) {

        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View list;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            list = new View(context);


            list = inflater.inflate(R.layout.searchresultsitem, null);

            // set value into textview
            TextView name = (TextView) list.findViewById(R.id.txtresult);

            try{

                name.setText(places.get(i).getName());

            }catch(Exception exc){

            }


        } else {
            list = (View) convertView;
        }

        return list;
    }
}
