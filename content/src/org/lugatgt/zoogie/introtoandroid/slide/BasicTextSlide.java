/*
 * Copyright (C) 2011 Michael Imamura
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.lugatgt.zoogie.introtoandroid.slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;


/**
 * Standard slide with a bullet-list of text.
 * @author Michael Imamura
 */
public class BasicTextSlide extends SlideFragment {

    private int textRes;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    /**
     * Constructor.
     * @param textRes A string-array resource.  Each item in the array
     *                corresponds to a bullet-list item.
     */
    public BasicTextSlide(int textRes) {
        this.textRes = textRes;
    }
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TableLayout view = (TableLayout)inflater.inflate(R.layout.slide_basic_text, null);
        
        CharSequence[] textRows = getResources().getTextArray(textRes);
        for (CharSequence textRow : textRows) {
            TableRow row = (TableRow)inflater.inflate(R.layout.basic_text_row, null);
            TextView textView = (TextView)row.findViewById(R.id.basicText_text);
            textView.setText(textRow);
            view.addView(row);
        }
        
        return view;
    }
    
}
