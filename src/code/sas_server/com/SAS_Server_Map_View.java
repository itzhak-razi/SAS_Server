package code.sas_server.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SAS_Server_Map_View extends MapActivity
{
	MapView mapView; 
    MapController mc;
    GeoPoint p;
    String longitude, latitude;
    SAS_Server_CustomOverlay overlayLoc; //this object draw the accuracy radius
    String accuracy; // the accuracy from the message
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pushpin_red_70_33);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
 
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.displayZoomControls(true);
        mapView.setBuiltInZoomControls(true);
 
        mc = mapView.getController();
        
        longitude = SAS_Server_Main.longitude_xml.getText().toString();
    	latitude = SAS_Server_Main.latitude_xml.getText().toString();
                
//        String coordinates[] = {"31.821273", "35.244784"}; HOME 
//        String coordinates[] = {"31.769633", "35.193355"}; //Collegue
        
    	String coordinates[] = {latitude,longitude};
        
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
        
        mc.animateTo(p);
        mc.setZoom(17);
        
        overlayLoc = new SAS_Server_CustomOverlay();
        overlayLoc.setSource(p, Float.parseFloat(SAS_Server_Main.accuracy));
        mapView.getOverlays().add(overlayLoc);
 
        mapView.invalidate();
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
