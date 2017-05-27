package world.selfiewithdaughter.www.selfiewithdaughter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SchemesAdapter extends ArrayAdapter<Schemes>{

    public SchemesAdapter(Context context, List<Schemes> schemes) {
        super(context, 0, schemes);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.schemes_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Schemes currentItem = getItem(position);

        TextView listHeader = (TextView) listItemView.findViewById(R.id.list_header);
        listHeader.setText(currentItem.getHeader());

        TextView listDescription = (TextView) listItemView.findViewById(R.id.list_description);
        listDescription.setText(currentItem.getDescription());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
