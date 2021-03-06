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

package org.lugatgt.zoogie.introtoandroid;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.ui.PresentationActivity;


/**
 * Main presentation activity.
 * <p>
 * This subclass of {@link PresentationActivity} adds our own content-specific
 * menu items and other customizations.
 * 
 * @author Michael Imamura
 */
public class MainActivity extends PresentationActivity {

    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public MainActivity() {
        super(new MainPresentation());
    }
    
    // LIFECYCLE ///////////////////////////////////////////////////////////////
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // First add the default menu items, then append our own.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.presentation_extra, menu);
        return true;
    }
    
    // EVENTS //////////////////////////////////////////////////////////////////
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
