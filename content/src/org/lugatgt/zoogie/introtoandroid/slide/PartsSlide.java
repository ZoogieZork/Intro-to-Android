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

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.introtoandroid.ui.PartLayer;
import org.lugatgt.zoogie.introtoandroid.ui.PartsView;
import org.lugatgt.zoogie.present.ui.SlideFragment;


/**
 * Overview of the different "layers" of parts in an app.
 * @author Michael Imamura
 */
public class PartsSlide extends SlideFragment {

    private enum Layer {
        SYSTEM(R.drawable.part_system, R.string.parts_system),
        APPLICATION(R.drawable.part_application, R.string.parts_application),
        ACTIVITY(R.drawable.part_activity, R.string.parts_activity),
        FRAGMENTS(R.drawable.part_fragments, R.string.parts_fragments),
        VIEWS(R.drawable.part_views, R.string.parts_views);
        
        private int imageRes;
        private int labelRes;
        
        private Layer(int imageRes, int labelRes) {
            this.imageRes = imageRes;
            this.labelRes = labelRes;
        }
        
        public int getImageRes() {
            return imageRes;
        }
        
        public int getLabelRes() {
            return labelRes;
        }
    }
    
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
            partsView.addLayer(new PartLayer(layer.getImageRes(), R.drawable.part_bg, layer.getLabelRes()));
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
    	ObjectAnimator toggle;
        if (partsView.getExpandAmount() < 1.0f) {
            toggle = ObjectAnimator.ofFloat(partsView, "expandAmount", 0.0f, 1.0f);
        } else {
            toggle = ObjectAnimator.ofFloat(partsView, "expandAmount", 1.0f, 0.0f);
        }
        toggle.setInterpolator(new DecelerateInterpolator());
        toggle.setDuration(800);
        toggle.start();
    }

}
