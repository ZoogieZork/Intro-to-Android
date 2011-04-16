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

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Displays the title.
 * This fragment may expand, depending on the current slide.
 * @author Michael Imamura
 */
public class TitleFragment extends Fragment {

    private static final String TITLE_KEY = "title";
    private static final String SUBTITLE_KEY = "subtitle";
    
    private ForegroundColorSpan subtitleColorSpan;
    
    private TextView titleLbl;
    private TextView titleAnimLbl;
    
    private CharSequence title = "";
    private CharSequence subtitle = "";
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title, null);
        
        subtitleColorSpan = new ForegroundColorSpan(
            getResources().getColor(R.color.subtitle_fg));
        
        titleLbl = (TextView)view.findViewById(R.id.titleLbl);
        titleAnimLbl = (TextView)view.findViewById(R.id.titleAnimLbl);
        
        if (savedInstanceState != null) {
            title = savedInstanceState.getCharSequence(TITLE_KEY);
            subtitle = savedInstanceState.getCharSequence(SUBTITLE_KEY);
            updateUi(false);
        }
        
        return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putCharSequence(TITLE_KEY, title);
        outState.putCharSequence(SUBTITLE_KEY, subtitle);
    }
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    /**
     * Update the title to reflect a new slide.
     * @param slide The new slide (may not be null).
     * @param animate true to animate between the previous title and the new title.
     */
    public void setSlide(Slide slide, boolean animate) {
        CharSequence newTitle = slide.getTitle(getActivity());
        if (newTitle == null) newTitle = "";
        
        CharSequence newSubtitle = slide.getSubtitle(getActivity());
        if (newSubtitle == null) newSubtitle = "";
        
        // Do nothing if the title and subtitle are unchanged.
        if (newTitle.equals(title) && newSubtitle.equals(subtitle)) return;
        
        title = newTitle;
        subtitle = newSubtitle;
        updateUi(animate);
    }
    
    private void updateUi(boolean animate) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(title);
        if (subtitle.length() > 0) {
            sb.append(": ").append(subtitle);
            sb.setSpan(subtitleColorSpan, title.length() + 2, sb.length(), 0);
        }
        
        if (animate) {
            titleAnimLbl.setText(titleLbl.getText());
            titleLbl.setAlpha(0.0f);
            titleLbl.setText(sb);
            ObjectAnimator.ofFloat(titleLbl, "alpha", 0.0f, 1.0f).setDuration(500).start();
            ObjectAnimator.ofFloat(titleAnimLbl, "alpha", 1.0f, 0.0f).setDuration(500).start();
        } else {
            titleLbl.setText(sb);
        }
    }
    
}
