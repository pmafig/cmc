package nsop.neds.cascais360;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nsop.neds.cascais360.Manager.Broadcast.AppSignatureHelper;
import nsop.neds.cascais360.Manager.MenuManager;
import nsop.neds.cascais360.Manager.SessionManager;
import nsop.neds.cascais360.Manager.WeatherManager;
import nsop.neds.cascais360.Settings.Settings;
import nsop.neds.cascais360.WebApi.WebApiCalls;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();

        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        menuFragment = findViewById(R.id.menu);

        new WeatherManager(this, (LinearLayout) findViewById(R.id.wearther)).execute(WebApiCalls.getWeather());

        new MenuManager(this, toolbar, menuFragment, Settings.labels.MyProfile);

        SessionManager sm = new SessionManager(this);

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText(Settings.labels.Welcome);

        TextView userName = findViewById(R.id.userName);
        userName.setText(sm.getDisplayname());

        TextView titleProfile = findViewById(R.id.account_settings_title);
        titleProfile.setText(Settings.labels.AccountSettings);
        titleProfile.setTextColor(Color.parseColor(Settings.colors.YearColor));

        TextView personalData = findViewById(R.id.personal_data);
        personalData.setText(Settings.labels.PersonalData);

        TextView passwordChange = findViewById(R.id.change_password);
        passwordChange.setText(Settings.labels.PasswordRecoveryNewPassword);

        TextView notifications = findViewById(R.id.notifications);
        notifications.setText(Settings.labels.Notifications);

        personalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditAccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //TODO Validar se o utilizador está logado

    }


    @Override
    public Intent getParentActivityIntent() {
        Intent parentIntent = getIntent();
        return parentIntent;
    }

}
