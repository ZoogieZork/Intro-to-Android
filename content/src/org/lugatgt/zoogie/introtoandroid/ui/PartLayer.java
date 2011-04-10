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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import org.lugatgt.zoogie.introtoandroid.R;


/**
 * A single layer in a {@link PartsView}.
 * @author Michael Imamura
 */
public class PartLayer {

    private int foregroundDrawable;
    
    private ImageView foregroundImage;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    public PartLayer(int foregroundDrawable) {
        this.foregroundDrawable = foregroundDrawable;
    }
    
    // VIEW ////////////////////////////////////////////////////////////////////
    
    /**
     * Create and initialize the view.
     * <p>
     * This is called by the container {@link PartsView} when a layer is added.
     * @return The initialized view (never null).
     */
    public View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.part_layer, null);
        
        foregroundImage = (ImageView)view.findViewById(R.id.partLayer_image);
        foregroundImage.setImageResource(foregroundDrawable);
        
        return view;
    }
    
    /**
     * Called by the container {@link PartsView}.
     * @param expandAmount How far the container is expanded (0.0 to 1.0).
     * @param translationX The image X translation distance, in pixels.
     * @param translationY The image Y translation distance, in pixels.
     * @param rotationX The image X rotation, in degrees.
     * @param rotationY The image Y rotation, in degrees.
     */
    public void setViewState(float expandAmount, int translationX, int translationY,
        float rotationX, float rotationY)
    {
        if (foregroundImage == null) return;
        
        foregroundImage.setTranslationX(translationX);
        foregroundImage.setTranslationY(translationY);
        foregroundImage.setRotationX(rotationX);
        foregroundImage.setRotationY(rotationY);
    }
    
}