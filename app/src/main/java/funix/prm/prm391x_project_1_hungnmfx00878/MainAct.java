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

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

public class MainAct extends AppCompatActivity {

    Button btnSMS, btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        btnSMS = (Button) findViewById(R.id.btn_mess);
        btnCall = findViewById(R.id.btn_phone);

        initViews();
    }

    //Initial views & function of button
    private void initViews() {
        btnSMS.setOnClickListener(view -> {
            makeDelayMessage();
            //Alpha effect for Message button
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            btnSMS.startAnimation(animation);
        });

        btnCall.setOnClickListener(view -> {
            makeDelayCall();
            //Alpha effect for Phone button
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            btnCall.startAnimation(animation);
        });
    }

    //Animation for transition to act_sms
    private void makeDelayMessage() {
        startActivity(new Intent(this, SMSAct.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //Animation for transition to act_phone
    private void makeDelayCall() {
        startActivity(new Intent(this, PhoneAct.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}