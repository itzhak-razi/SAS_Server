package code.sas_server.com;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SAS_Server_CustomOverlay extends Overlay {

	private GeoPoint sourcePoint;
	private float accuracy;

	public SAS_Server_CustomOverlay() {
		super();
	}

	public void setSource(GeoPoint geoPoint, float accuracy) {
		sourcePoint = geoPoint;
		this.accuracy = accuracy;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
		Projection projection = mapView.getProjection();
		Point center = new Point();

		int radius = (int) (projection.metersToEquatorPixels(accuracy));
		projection.toPixels(sourcePoint, center);

		Paint accuracyPaint = new Paint();
		accuracyPaint.setAntiAlias(true);
		accuracyPaint.setStrokeWidth(2.0f);
		accuracyPaint.setColor(0xff6666ff);
		accuracyPaint.setStyle(Style.STROKE);

		canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

		accuracyPaint.setColor(0x186666ff);
		accuracyPaint.setStyle(Style.FILL);
		canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

	}

}
