package com.jjoe64.graphview;

import android.app.Activity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView.GraphViewData;

/**
 * GraphViewDemo creates some dummy data to demonstrate the GraphView component.
 *
 * IMPORTANT: For examples take a look at GraphView-Demos (https://github.com/jjoe64/GraphView-Demos)
 *
 * Copyright (C) 2011 Jonas Gehring
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */
public class GraphViewDemo extends Activity {
	/**
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		LineGraphView graphView = new LineGraphView(
				this
				, "GraphViewDemo"
		);
		*/
		MinMaxGraphView graphView = new MinMaxGraphView(
				this
				, "GraphViewDemo"
		);

		graphView.addSeries(new GraphViewSeries(new GraphViewData[] {
				new GraphViewData(1, 2.0d, 1.9, 2.1) // real value, min deviation, max deviation
				, new GraphViewData(2, 1.5d, 1.4, 1.6)
				, new GraphViewData(2.5, 3.0d, 2.9, 3.1)
				, new GraphViewData(3, 2.5d, 2.4, 2.6)
				, new GraphViewData(4, 1.0d, 0.9, 1.1)
				, new GraphViewData(5, 3.0d, 2.9, 3.1)
		}));
		setContentView(graphView);
	}
}
