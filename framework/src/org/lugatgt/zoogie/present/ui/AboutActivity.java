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

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lugatgt.zoogie.present.R;


/**
 * Base class for "About" activity, for displaying info about the presentation.
 * <p>
 * This is somewhat an abuse of the new style of {@link PreferenceActivity} in
 * Honeycomb.  It gives us a set of tabs ("headers") which switch between
 * content fragments.  We don't actually use any of the features for loading
 * or modifying preferences here.
 * <p>
 * Subclasses can add their own content by overriding {@link #onBuildHeaders(List)}.
 * 
 * @author Michael Imamura
 */
public abstract class AboutActivity extends PreferenceActivity {

    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.about_pref_headers, target);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // LicenseFragment
    
    public static class LicenseFragment extends Fragment {
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.about_license, null);
            
            TextView licenseTxt = (TextView)view.findViewById(R.id.licenseTxt);
            licenseTxt.setMovementMethod(LinkMovementMethod.getInstance());
            
            return view;
        }
        
    }
    
}
