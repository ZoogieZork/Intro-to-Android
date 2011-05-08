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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.lugatgt.zoogie.present.Presentation;
import org.lugatgt.zoogie.present.R;


/**
 * Base class for the fragment for a slide.
 * @author Michael Imamura
 */
public abstract class SlideFragment extends Fragment {

    private Presentation presentation;
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    /**
     * Retrieve the presentation this slide fragment is attached to (if any).
     * @return The presentation, or null if this slide is not the active (visible)
     *         slide.
     */
    protected Presentation getPresentation() {
        return presentation;
    }
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.slide, null);
        
        View contentView = onCreateContentView(inflater, view, savedInstanceState);
        if (contentView != null) {
            FrameLayout contentFrame = (FrameLayout)view.findViewById(R.id.contentFrame);
            contentFrame.addView(contentView);
        }
        
        return view;
    }
    
    /**
     * Called when this fragment is attached to the presentation.
     * <p>
     * This occurs before the fragment about to become the active
     * (visible) slide, before the fragment is attached to the activity.
     * 
     * @param presentation The active presentation this slide is becoming part of.
     */
    public void onAttachToPresentation(Presentation presentation) {
        this.presentation = presentation;
    }
    
    /**
     * Called when this fragment is detached from the presentation.
     * <p>
     * This occurs when the fragment is no longer the active (visible) slide,
     * before the fragment is detached from the activity.
     */
    public void onDetachFromPresentation() {
        presentation = null;
    }
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    
}
