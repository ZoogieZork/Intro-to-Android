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

package org.lugatgt.zoogie.present.ui;

import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.Slide;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Displays the title.
 * This fragment may expand, depending on the current slide.
 * @author Michael Imamura
 */
public class TitleFragment extends Fragment {

    private TextView titleLbl;
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title, null);
        
        titleLbl = (TextView)view.findViewById(R.id.titleLbl);
        
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState != null) {
            titleLbl.setText(savedInstanceState.getCharSequence("title"));
        }
    }
    
    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putCharSequence("title", titleLbl.getText());
    }
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    public void setSlide(Slide slide) {
        //TODO: We'd actually want to get the formatted title of the slide.
        //TODO: Support for subtitles.
        CharSequence newTitle = slide.getTitle(getActivity());
        if (newTitle == null) newTitle = "";
        
        // Do nothing if the title is unchanged.
        if (newTitle.equals(titleLbl.getText())) return;
        
        titleLbl.setText(newTitle);
    }
    
}
