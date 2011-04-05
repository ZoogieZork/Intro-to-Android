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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;


/**
 * Base class for presentations.
 * @author Michael Imamura
 */
public abstract class Presentation {
    
    private static final String PROP_PFX = "presentation.";
    private static final String PROP_INDEX = PROP_PFX + "index";
    
    private List<Slide> slides;
    private Map<String, Slide> nameToSlide;
    
    private int idx;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    /**
     * Constructor.
     * @param initSlides The initial set of slides (may not be null, may not be empty).
     */
    public Presentation(Slide[] initSlides) {
        if (initSlides == null || initSlides.length == 0) {
            throw new IllegalArgumentException("Initial slides must not be empty.");
        }
        
        slides = Arrays.asList(initSlides);
        
        nameToSlide = new HashMap<String, Slide>();
        for (Slide initSlide : initSlides) {
            nameToSlide.put(initSlide.getName(), initSlide);
        }
        
        idx = 0;
    }
    
    // SLIDES //////////////////////////////////////////////////////////////////
    
    public CharSequence[] getSlideTitles() {
        CharSequence[] titles = new CharSequence[slides.size()];
        for (int i = 0; i < slides.size(); i++) {
            titles[i] = slides.get(i).getTitle();
        }
        return titles;
    }
    
    public Slide getCurrentSlide() {
        return slides.get(idx);
    }
    
    public int getCurrentSlideIndex() {
        return idx;
    }
    
    public boolean isAtBeginning() {
        return idx == 0;
    }
    
    public boolean isAtEnd() {
        return idx == slides.size() - 1;
    }
    
    public int getSlideCount() {
        return slides.size();
    }
    
    public Slide jumpTo(int i) {
        if (i < 0 || i >= slides.size()) {
            throw new ArrayIndexOutOfBoundsException(
                "Slide index must be 0 <= idx < " + slides.size());
        }
        idx = i;
        return getCurrentSlide();
    }
    
    public Slide prev() {
        if (idx == 0) {
            return null;
        } else {
            idx--;
            return getCurrentSlide();
        }
    }
    
    public Slide next() {
        if (idx == slides.size() - 1) {
            return null;
        } else {
            idx++;
            return getCurrentSlide();
        }
    }
    
    // PERSISTANCE /////////////////////////////////////////////////////////////
    
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        idx = savedInstanceState.getInt(PROP_INDEX);
    }
    
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PROP_INDEX, idx);
    }
    
}
