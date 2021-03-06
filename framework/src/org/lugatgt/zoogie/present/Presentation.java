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

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;


/**
 * Base class for presentations.
 * @author Michael Imamura
 */
public abstract class Presentation {
    
    private static final String PROP_PFX = "presentation.";
    private static final String PROP_INDEX = PROP_PFX + "index";
    
    private List<Slide> slides;
    private Map<String, Integer> nameToIndex;
    
    private OnIndexChangedListener indexChangedListener;
    
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
        
        nameToIndex = new HashMap<String, Integer>();
        for (int i = 0; i < initSlides.length; i++) {
            Slide initSlide = initSlides[i];
            nameToIndex.put(initSlide.getName(), i);
        }
        
        idx = 0;
    }
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    /**
     * Set the callback for when the current slide index changes.
     * @param listener The listener (may be null).
     */
    public void setOnIndexChangedListener(OnIndexChangedListener listener) {
        indexChangedListener = listener;
    }
    
    // SLIDES //////////////////////////////////////////////////////////////////
    
    /**
     * Retrieve a list of combined slide titles (title and subtitle).
     * @param ctx The current context, to load resources (may not be null).
     * @return The list of titles (never null, never empty).
     */
    public CharSequence[] getSlideTitles(Context ctx) {
        CharSequence[] titles = new CharSequence[slides.size()];
        for (int i = 0; i < slides.size(); i++) {
            titles[i] = generateSlideTitle(ctx, slides.get(i), i);
        }
        return titles;
    }
    
    /**
     * Generate the combined slide title (title and subtitle).
     * @param ctx The current context, to load resources (may not be null).
     * @param slide The slide (may not be null).
     * @param idx The zero-based slide index.
     * @return The combined slide title (never null, may be empty).
     */
    protected CharSequence generateSlideTitle(Context ctx, Slide slide, int idx) {
        CharSequence title = slide.getTitle(ctx);
        CharSequence subtitle = slide.getSubtitle(ctx);
        
        CharSequence combined;
        if (subtitle != null && subtitle.length() > 0) {
            combined = new SpannableStringBuilder().append(title).append(": ").append(subtitle);
        } else {
            combined = title;
        }
        
        return combined;
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
        Slide oldSlide = getCurrentSlide();
        int oldIdx = idx;
        idx = i;
        fireOnIndexChanged(oldSlide, oldIdx, true);
        return getCurrentSlide();
    }
    
    public Slide jumpTo(String name) {
        Integer newIdx = nameToIndex.get(name);
        if (newIdx == null) {
            throw new IllegalArgumentException("Unable to find a slide named \"" + name + '"');
        } else {
            return jumpTo(newIdx);
        }
    }
    
    public Slide prev() {
        if (idx == 0) {
            return null;
        } else {
            Slide oldSlide = getCurrentSlide();
            idx--;
            fireOnIndexChanged(oldSlide, idx + 1, true);
            return getCurrentSlide();
        }
    }
    
    public Slide next() {
        if (idx == slides.size() - 1) {
            return null;
        } else {
            Slide oldSlide = getCurrentSlide();
            idx++;
            fireOnIndexChanged(oldSlide, idx - 1, false);
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
    
    // EVENTS //////////////////////////////////////////////////////////////////
    
    protected void fireOnIndexChanged(Slide oldSlide, int oldIndex, boolean immediate) {
        if (indexChangedListener != null) {
            if (oldIndex != idx) {
                indexChangedListener.onAfterIndexChanged(oldSlide, oldIndex, immediate);
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // OnIndexChangedListener
    
    public interface OnIndexChangedListener {
        
        /**
         * Called when the current slide index changes.
         * <p>
         * Implementations can find the new slide and slide index by calling
         * {@link #getCurrentSlide()} and {@link #getCurrentSlideIndex()},
         * respectively.
         * 
         * @param oldSlide The old slide (may be null).
         * @param oldIndex The old slide index.
         * @param immediate true if navigating to the new slide immediately;
         *                  (i.e., should not play any animations).
         */
        void onAfterIndexChanged(Slide oldSlide, int oldIndex, boolean immediate);
        
    }
    
}
