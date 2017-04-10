package org.artek.app.main;

import android.app.Fragment;

public abstract class ArtekFragment extends Fragment{
    private boolean needVK;


    public void init(boolean needVK){
        this.needVK = needVK;
    }

    public boolean isNeedVK() {
        return needVK;
    }
}
