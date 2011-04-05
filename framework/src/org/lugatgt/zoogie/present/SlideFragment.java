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

package org.lugatgt.zoogie.present;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**
 * Base class for the fragment for a slide.
 * @author Michael Imamura
 */
public abstract class SlideFragment extends Fragment {

    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.slide, null);
        
        View contentView = onCreateContentView(inflater, view, savedInstanceState);
        if (contentView != null) {
            view.addView(contentView);
        }
        
        return view;
    }
    
    // CONTENT /////////////////////////////////////////////////////////////////
    
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    
}
