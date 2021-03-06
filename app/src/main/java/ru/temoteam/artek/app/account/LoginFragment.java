package ru.temoteam.artek.app.account;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ru.temoteam.artek.app.R;
import ru.temoteam.artek.app.main.ArtekFragment;


public class LoginFragment extends ArtekFragment {

    View.OnClickListener loginVK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(ru.temoteam.artek.app.R.id.frgmCont, new LoginVKFragment()).addToBackStack(null).commit();
        }
    };
    View.OnClickListener openArtek = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.artek.org"));
            startActivity(browserIntent);
        }
    };

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.init(false);
        return inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title = getString(R.string.login_vk);
        Button login_vk = (Button) getView().findViewById(ru.temoteam.artek.app.R.id.imageButtonLoginVk);
        login_vk.setOnClickListener(loginVK);

        ImageView open_artek = (ImageView) getView().findViewById(ru.temoteam.artek.app.R.id.imageLogoOpenArtek);
        open_artek.setOnClickListener(openArtek);

    }



}
