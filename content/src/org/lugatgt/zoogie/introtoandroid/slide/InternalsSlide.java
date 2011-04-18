package org.lugatgt.zoogie.introtoandroid.slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lugatgt.zoogie.introtoandroid.R;
import org.lugatgt.zoogie.present.ui.SlideFragment;


public class InternalsSlide extends SlideFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_internals, null);
        
        return view;
    }

}
