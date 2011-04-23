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
 * Standard slide transitions provided by the framework.
 * @author Michael Imamura
 */
public interface Transitions {
    
    SlideTransition FADE = new SlideTransition(android.R.animator.fade_out, android.R.animator.fade_in);
    SlideTransition SPIN = new SlideTransition(R.anim.spin_out, R.anim.spin_in);
    SlideTransition SLIDE_LEFT = new SlideTransition(R.anim.fade_slide_left_out, R.anim.fade_slide_left_in);
    
    SlideTransition SLIDE_UP = new SlideTransition(R.anim.fade_slide_up_out, R.anim.fade_slide_up_in);
    
    SlideTransition ZOOM_IN = new SlideTransition(R.anim.fade_zoom_in_out, R.anim.fade_zoom_in_in);
    
}
