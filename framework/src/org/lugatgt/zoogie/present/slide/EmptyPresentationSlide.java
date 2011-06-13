package org.lugatgt.zoogie.present.slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lugatgt.zoogie.present.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;


/**
 * "Dummy" slide used when the presentation has no slides.
 * @author Michael Imamura
 */
public class EmptyPresentationSlide extends SlideFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slide_internal_empty_presentation, null);
    }

}
