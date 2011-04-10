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

package org.lugatgt.zoogie.introtoandroid.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;


/**
 * An expandable stack of image layers, with labels.
 * @author Michael Imamura
 */
public class PartsView extends FrameLayout {

    private int expandDistanceX = 120;
    private int expandDistanceY = 120;
    private float rotationX = -30.0f;
    private float rotationY = 30.0f;
    
    private float expandAmount = 0.0f;
    
    private List<PartLayer> layers = new ArrayList<PartLayer>(5);
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public PartsView(Context ctx) {
        super(ctx);
    }
    
    public PartsView(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    
    public PartsView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    public int getExpandDistanceX() {
        return expandDistanceX;
    }

    public void setExpandDistanceX(int expandDistanceX) {
        if (this.expandDistanceX != expandDistanceX) {
            this.expandDistanceX = expandDistanceX;
            updateLayerLayout();
        }
    }

    public int getExpandDistanceY() {
        return expandDistanceY;
    }

    public void setExpandDistanceY(int expandDistanceY) {
        if (this.expandDistanceY != expandDistanceY) {
            this.expandDistanceY = expandDistanceY;
            updateLayerLayout();
        }
    }
    
    /**
     * Retrieve how far this stack is expanded.
     * @return The distance, from 0 (not expanded) to 1 (fully expanded).
     */
    public float getExpandAmount() {
        return expandAmount;
    }
    
    /**
     * Set how far this stack is expanded.
     * @param expandAmount The distance, from 0 (not expanded) to 1 (fully
     *                     expanded).  Values outside of the range 0.0-1.0 are
     *                     clamped to the range.
     */
    public void setExpandAmount(float expandAmount) {
        if (expandAmount < 0.0f) expandAmount = 0.0f;
        else if (expandAmount > 1.0f) expandAmount = 1.0f;
        
        if (this.expandAmount != expandAmount) {
            this.expandAmount = expandAmount;
            updateLayerLayout();
        }
    }

    // LAYER MANAGEMENT ////////////////////////////////////////////////////////
    
    /**
     * Add a layer to front of the stack.
     * <p>
     * This may only be called from the UI thread.
     * @param layer The layer to add (may not be null).
     */
    public void addLayer(PartLayer layer) {
        layers.add(layer);
        
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layer.onCreateView(inflater));
        updateLayerLayout();
    }
    
    protected void updateLayerLayout() {
        if (layers.isEmpty()) return;
        
        int numLayers = layers.size();
        float rotX = rotationX * expandAmount;
        float rotY = rotationY * expandAmount;
        float transStepX = ((float)expandDistanceX / (float)(numLayers - 1)) * expandAmount;
        float transStepY = ((float)expandDistanceY / (float)(numLayers - 1)) * expandAmount;
        
        float curTransX = 0.0f;
        float curTransY = 0.0f;
        
        for (PartLayer layer : layers) {
            layer.setViewState(expandAmount, (int)curTransX, (int)curTransY, rotX, rotY);
            curTransX += transStepX;
            curTransY += transStepY;
        }
    }
    
}
