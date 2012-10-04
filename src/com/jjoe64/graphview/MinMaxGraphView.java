package com.jjoe64.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * MinMax Graph View. This draws a line chart with minmax spread of data in points.
 * @author jjoe64 - jonas gehring - http://www.jjoe64.com
 *
 * Copyright (C) 2011 Jonas Gehring
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */
public class MinMaxGraphView extends GraphView {
	private final Paint paintBackground;
	private final int deviationAlpha = 55;
	private boolean drawBackground;

	Canvas canvas;
	Paint paintDeviation;

	public MinMaxGraphView(Context context, String title) {
		super(context, title);

		paintBackground = new Paint();
		paintBackground.setARGB(180, 20, 40, 60);
		paintBackground.setStrokeWidth(4);
		
		paintDeviation = new Paint();
		paintDeviation.setStrokeWidth(4);
	}

	/**
	 * returns the maximal Y value of all data.
	 *
	 * warning: only override this, if you really know want you're doing!
	 */
	@Override
	protected double getMaxY() {
		double largest;
		if (manualYAxis) {
			largest = manualMaxYValue;
		} else {
			largest = Integer.MIN_VALUE;
			for (int i=0; i<graphSeries.size(); i++) {
				GraphViewData[] values = _values(i);
				for (int ii=0; ii<values.length; ii++) {
					if (values[ii].valueY > largest) 
						largest = values[ii].valueY;

					if (values[ii].maxValueY > largest) 
						largest = values[ii].maxValueY;
				}
			}
		}
		return largest;
	}

	/**
	 * returns the minimal Y value of all data.
	 *
	 * warning: only override this, if you really know want you're doing!
	 */
	protected double getMinY() {
		double smallest;
		if (manualYAxis) {
			smallest = manualMinYValue;
		} else {
			smallest = Integer.MAX_VALUE;
			for (int i=0; i<graphSeries.size(); i++) {
				GraphViewData[] values = _values(i);
				for (int ii=0; ii<values.length; ii++) {
					if (values[ii].valueY < smallest)
						smallest = values[ii].valueY;

					if (values[ii].minValueY < smallest)
						smallest = values[ii].minValueY;
				}
			}
		}
		return smallest;
	}	

	@Override
	public void drawSeries(Canvas canvas, GraphViewData[] values, float graphwidth, 
			float graphheight, float border, double minX, double minY, 
			double diffX, double diffY, float horstart) {
		this.canvas = canvas;
		// draw background
		double lastEndY = 0;
		double lastEndX = 0;

		double lastDevMin = 0;
		double lastDevMax = 0;

		paintDeviation.setColor(paint.getColor());
		paintDeviation.setAlpha(deviationAlpha);

		if (drawBackground) {
			float startY = graphheight + border;
			for (int i = 0; i < values.length; i++) {
				double valY = values[i].valueY - minY;
				double ratY = valY / diffY;
				double y = graphheight * ratY;

				double valX = values[i].valueX - minX;
				double ratX = valX / diffX;
				double x = graphwidth * ratX;

				float endX = (float) x + (horstart + 1);
				float endY = (float) (border - y) + graphheight +2;

				if (i > 0) {
					// fill space between last and current point
					int numSpace = (int) ((endX - lastEndX) / 3f) +1;
					for (int xi=0; xi<numSpace; xi++) {
						float spaceX = (float) (lastEndX + ((endX-lastEndX)*xi/(numSpace-1)));
						float spaceY = (float) (lastEndY + ((endY-lastEndY)*xi/(numSpace-1)));

						// start => bottom edge
						float startX = spaceX;

						// do not draw over the left edge
						if (startX-horstart > 1) {
							canvas.drawLine(startX, startY, spaceX, spaceY, paintBackground);
						}
					}
				}

				lastEndY = endY;
				lastEndX = endX;
			}
		}

		//draw fill, first point is max value
		Path path = new Path();

		lastEndY = 0;
		lastEndX = 0;

		lastDevMin = 0;
		lastDevMax = 0;

		int length = values.length;
		for (int i = 0; i < length; i++) {
			double valY = values[i].valueY - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY;

			double valX = values[i].valueX - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;

			double valYmin = values[i].minValueY - minY;
			double ratYmin = valYmin / diffY;
			double yDeviationMin = graphheight * ratYmin;

			double valYmax = values[i].maxValueY - minY;
			double ratYmax = valYmax / diffY;
			double yDeviationMax = graphheight * ratYmax;

			float startX = (float) lastEndX + (horstart + 1);
			float endX = (float) x + (horstart + 1);

			float devYmax = (float) (border - lastDevMax) + graphheight;

			if (i > 0) {
				if (i == 1) {
					path.moveTo(startX, devYmax);
				}
				else {
					path.lineTo(startX, devYmax);
				}
			}

			lastEndY = y;
			lastEndX = x;

			lastDevMin = yDeviationMin;
			lastDevMax = yDeviationMax;

			if (i == length-1) {
				devYmax = (float) (border - lastDevMax) + graphheight;
				path.lineTo(endX, devYmax);
			}
		}

		//draw fill, continue to min value
		lastEndY = 0;
		lastEndX = 0;

		lastDevMin = 0;
		lastDevMax = 0;
		for (int i = length-1; i > -1; i--) {
			double valY = values[i].valueY - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY;

			double valX = values[i].valueX - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;

			double valYmin = values[i].minValueY - minY;
			double ratYmin = valYmin / diffY;
			double yDeviationMin = graphheight * ratYmin;

			double valYmax = values[i].maxValueY - minY;
			double ratYmax = valYmax / diffY;
			double yDeviationMax = graphheight * ratYmax;

			float startX = (float) lastEndX + (horstart + 1);
			float endX = (float) x + (horstart + 1);

			float devYmin = (float) (border - lastDevMin) + graphheight;

			lastEndY = y;
			lastEndX = x;

			lastDevMin = yDeviationMin;
			lastDevMax = yDeviationMax;

			if (i < length-1) {
				path.lineTo(startX, devYmin);
			}

			if (i == 0) {
				devYmin = (float) (border - lastDevMin) + graphheight;
				path.lineTo(endX, devYmin);
			}
		}

		path.close();
		canvas.drawPath(path, paintDeviation);


		// draw data
		lastEndY = 0;
		lastEndX = 0;

		lastDevMin = 0;
		lastDevMax = 0;

		// draw data
		for (int i = 0; i < length; i++) {

			double valY = values[i].valueY - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY;

			double valX = values[i].valueX - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;

			double valYmin = values[i].minValueY - minY;
			double ratYmin = valYmin / diffY;
			double yDeviationMin = graphheight * ratYmin;

			double valYmax = values[i].maxValueY - minY;
			double ratYmax = valYmax / diffY;
			double yDeviationMax = graphheight * ratYmax;

			float startX = (float) lastEndX + (horstart + 1);
			float startY = (float) (border - lastEndY) + graphheight;
			float endX = (float) x + (horstart + 1);
			float endY = (float) (border - y) + graphheight;

			if (i > 0) {
				canvas.drawLine(startX, startY, endX, endY, paint);
			}
			lastEndY = y;
			lastEndX = x;

			lastDevMin = yDeviationMin;
			lastDevMax = yDeviationMax;
		}
	}

	public boolean getDrawBackground() {
		return drawBackground;
	}

	/**
	 * @param drawBackground true for a light blue background under the graph line
	 */
	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}
}
