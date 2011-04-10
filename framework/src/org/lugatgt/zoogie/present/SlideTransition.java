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


/**
 * Defines a transition between two slides.
 * @author Michael Imamura
 */
public class SlideTransition {

    private int outAnimationRes;
    private int inAnimationRes;
    
    // CONSTRUCTORS ////////////////////////////////////////////////////////////
    
    /**
     * Constructor.
     * @param outAnimationRes The animation resource for the outgoing slide.
     * @param inAnimationRes The animation resource for the incoming slide.
     */
    public SlideTransition(int outAnimationRes, int inAnimationRes) {
        this.outAnimationRes = outAnimationRes;
        this.inAnimationRes = inAnimationRes;
    }
    
    // FIELD ACCESS ////////////////////////////////////////////////////////////
    
    /**
     * Retrieve the outgoing slide animation resource.
     * @return The resource ID.
     */
    public int getOutAnimationRes() {
        return outAnimationRes;
    }

    /**
     * Retrieve the incoming slide animation resource.
     * @return The resource ID.
     */
    public int getInAnimationRes() {
        return inAnimationRes;
    }

}
