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

import android.content.Context;

import org.lugatgt.zoogie.present.ui.SlideFragment;


/**
 * Metadata for a slide in the presentation.
 * @author Michael Imamura
 */
public interface Slide {
    
    /**
     * The unique ID of the slide (not visible to the user).
     * @return The unique ID (may not be null, may not be empty).
     */
    public String getName();
    
    /**
     * Retrieves the title of the slide.
     * @param ctx The context, for retrieving resources (may not be null).
     * @return The title, or null if the slide has no title.
     */
    public CharSequence getTitle(Context ctx);
    
    /**
     * Creates the slide fragment used for displaying the content.
     * @return The slide fragment (may be null).
     * @throws InstantiationException The fragment could not be created.
     * @throws IllegalAccessException The fragment could not be created.
     */
    public SlideFragment createFragment() throws InstantiationException, IllegalAccessException;
    
}
