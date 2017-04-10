package org.artek.app.account;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.artek.app.R;
import org.artek.app.main.ArtekFragment;


public class LoginFragment extends ArtekFragment {

    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.init(false);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button login_vk = (Button) getView().findViewById(R.id.imageButtonLoginVk);
        login_vk.setOnClickListener(loginVK);

        ImageView open_artek = (ImageView) getView().findViewById(R.id.imageLogoOpenArtek);
        open_artek.setOnClickListener(openArtek);

    }

    View.OnClickListener loginVK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.frgmCont, new LoginVKFragment()).addToBackStack(null).commit();
        }
    };

    View.OnClickListener openArtek = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.artek.org"));
            startActivity(browserIntent);
        }
    };



}
