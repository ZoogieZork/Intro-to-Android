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

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.introtoandroid.ui.PartLayer;
import org.lugatgt.zoogie.introtoandroid.ui.PartsView;
import org.lugatgt.zoogie.present.ui.SlideFragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Overview of the different "layers" of parts in an app.
 * @author Michael Imamura
 */
public class PartsSlide extends SlideFragment {

    private enum Layer {
        SYSTEM,
        APPLICATION,
        ACTIVITY,
        FRAGMENTS,
        VIEWS;
    }
    
    /* System (just status bar)
     * Application (holds activities)
     * Activity
     * Fragments
     * Views
     */
    
    private static final String EXPAND_AMOUNT_TAG = "expandAmount";
    
    private PartsView partsView;
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_parts, null);
        
        partsView = (PartsView)view.findViewById(R.id.parts);
        partsView.setExpandDistanceX(250);
        partsView.setExpandDistanceY(150);
        
        for (Layer layer : Layer.values()) {
            partsView.addLayer(new PartLayer(R.drawable.part_test));
        }
        
        partsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePartsExpand();
            }
        });
        
        if (savedInstanceState != null) {
            partsView.setExpandAmount(savedInstanceState.getFloat(EXPAND_AMOUNT_TAG, 0.0f));
        }
        
        return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putFloat(EXPAND_AMOUNT_TAG, partsView.getExpandAmount());
    }
    
    // ACTIONS /////////////////////////////////////////////////////////////////
    
    protected void togglePartsExpand() {
        ObjectAnimator animator;
        if (partsView.getExpandAmount() < 1.0f) {
            animator = ObjectAnimator.ofFloat(partsView, "expandAmount", 0.0f, 1.0f);
        } else {
            animator = ObjectAnimator.ofFloat(partsView, "expandAmount", 1.0f, 0.0f);
        }
        animator.setDuration(1000);
        animator.start();
    }

}
