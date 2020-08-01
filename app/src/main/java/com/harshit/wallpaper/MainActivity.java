package com.harshit.wallpaper;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.harshit.wallpaper.Fragment.ImageList;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FrameLayout container;

    DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((Advance3DDrawerLayout) drawer).setViewRotation(GravityCompat.START, 15);
        //((Advance3DDrawerLayout) drawer).setRadius(GravityCompat.START, 25);
        ((Advance3DDrawerLayout) drawer).setViewScale(GravityCompat.START, 0.9f); //set height scale for main view (0f to 1f)
        ((Advance3DDrawerLayout) drawer).setViewElevation(GravityCompat.START, 20); //set main view elevation when drawer open (dimension)
        ((Advance3DDrawerLayout) drawer).setViewScrimColor(GravityCompat.START, Color.TRANSPARENT); //set drawer overlay coloe (color)
        ((Advance3DDrawerLayout) drawer).setDrawerElevation(25f); //set drawer elevation (dimension)
        ((Advance3DDrawerLayout) drawer).setContrastThreshold(3); //set maximum of contrast ratio between white text and background color.
        ((Advance3DDrawerLayout) drawer).setRadius(GravityCompat.START, 25); //set end container's corner radius (dimension)

        getSupportFragmentManager().beginTransaction().replace(R.id.contatiner, new ImageList()).commit();

    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Do You Want To exit ?");
            builder.setTitle(R.string.app_name)
                    .setIcon(getResources().getDrawable(R.drawable.likeborder));
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(getApplicationContext(), Exit.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            getSupportFragmentManager().popBackStack();
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

}
