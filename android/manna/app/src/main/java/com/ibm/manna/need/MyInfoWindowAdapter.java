package com.ibm.manna.need;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.ibm.manna.R;

import java.util.StringTokenizer;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View myContentsView;
    Context context;
    public MyInfoWindowAdapter(Context context){
        this.context = context;
        myContentsView = LayoutInflater.from(context).inflate(R.layout.card_map_infowindow, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView line1 = myContentsView.findViewById(R.id.infowindow_line1);
        TextView line2 = myContentsView.findViewById(R.id.infowindow_line2);
        Button infobutton = myContentsView.findViewById(R.id.infobutton);

        StringTokenizer tokenizer = new StringTokenizer(marker.getSnippet(),",");
        line2.setText(tokenizer.nextToken());
        if(tokenizer.nextToken().equals("N")) {
            infobutton.setText("Share");
            line1.setText(marker.getTitle() + " is Needed");
            infobutton.setBackgroundColor(context.getColor(R.color.buttonBlue));
        }
        else {
            infobutton.setText("Request");
            line1.setText(marker.getTitle() + " is Available");
            infobutton.setBackgroundColor(context.getColor(R.color.buttonGreen));
        }
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
