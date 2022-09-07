/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package funix.prm.prm391x_project_1_hungnmfx00878;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhoneAct extends AppCompatActivity {
    EditText etTelNr, etTime;
    RadioGroup rgUnit;
    RadioButton rbUnit;
    Button btnCall;

    int time, delayTime;
    String telNr, unit;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_phone);

        etTelNr = findViewById(R.id.phone);
        etTime = findViewById(R.id.time);
        btnCall = findViewById(R.id.setup);

        //Create action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Phone");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
    }

    //Initial views & function of button
    public void initViews() {
        btnCall.setOnClickListener(view -> {
            telNr = etTelNr.getText().toString();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) { //Validate permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
            } else {
                if (telNr.isEmpty()) { //Validate input phone number & message
                    Toast.makeText(PhoneAct.this, getString(R.string.info), Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.PHONE.matcher(telNr).matches()) {
                    Toast.makeText(PhoneAct.this, getString(R.string.type), Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
                    } else {
                        setDelayTime();
                        mHandler.postDelayed(mCallRunnable, delayTime);

                        if (time > 1) {
                            Toast.makeText(getApplicationContext(), getString(R.string.phone_call) + " " + time + " " + unit + "s", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.phone_call) + " " + time + " " + unit, Toast.LENGTH_LONG).show();
                        }

                        finish();
                    }
                }
            }

            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(200);
            btnCall.startAnimation(animation);
        });
    }

    public void setDelayTime() {
        time = Integer.parseInt(etTime.getText().toString());
        rgUnit = findViewById(R.id.unit);

        rbUnit = findViewById(rgUnit.getCheckedRadioButtonId());
        unit = rbUnit.getText().toString();

        if (unit.equals("Hour")) {
            delayTime = time * 1000 * 60 * 60;
        } else if (unit.equals("Minute")) {
            delayTime = time * 1000 * 60;
        } else {
            delayTime = time * 1000;
        }
    }

    private final Runnable mCallRunnable = new Runnable() {
        @Override
        public void run() {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNr));
            startActivity(callIntent);
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
