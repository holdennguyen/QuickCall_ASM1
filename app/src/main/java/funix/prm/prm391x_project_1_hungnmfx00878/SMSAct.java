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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
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

public class SMSAct extends AppCompatActivity {
    EditText etTelNr, etMessage, etTime;
    RadioGroup rgUnit;
    RadioButton rbUnit;
    Button btnSMS;

    int time, delayTime;
    String telNr, message, unit;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sms);

        etMessage = findViewById(R.id.message);
        etTelNr = findViewById(R.id.phone);
        etTime = findViewById(R.id.time);
        btnSMS = findViewById(R.id.setup);

        //Create action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SMS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
    }

    //Initial views & function of button
    private void initViews() {
        btnSMS.setOnClickListener(view -> {
            message = etMessage.getText().toString();
            telNr = etTelNr.getText().toString();

            if (ContextCompat.checkSelfPermission(SMSAct.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) { //Validate permission
                ActivityCompat.requestPermissions(SMSAct.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                if (telNr.isEmpty()||message.isEmpty()) { //Validate input phone number & message
                    Toast.makeText(SMSAct.this, getString(R.string.info), Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.PHONE.matcher(telNr).matches()) { //Validate format of phone number
                    Toast.makeText(SMSAct.this, getString(R.string.type), Toast.LENGTH_SHORT).show();
                } else {
                    setDelayTime();
                    mHandler.postDelayed(mMessageRunnable, delayTime);

                    //Add plural for time unit
                    if (time > 1) {
                        Toast.makeText(getApplicationContext(), getString(R.string.sms_sent) + " " + time + " " + unit + "s", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.sms_sent) + " " + time + " " + unit, Toast.LENGTH_LONG).show();
                    }

                    //Return to act_main after setup successfully
                    finish();
                }
            }

            //Alpha effect for setup button
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(200);
            btnSMS.startAnimation(animation);
        });
    }

    //Calculate delay time base on checked radio button
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

    //Send SMS
    private final Runnable mMessageRunnable = new Runnable() {
        @Override
        public void run() {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(telNr, null, message, null, null);
        }
    };

    //Animation for transition return to act_main
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //Return to act_main
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
