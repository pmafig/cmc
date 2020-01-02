package nsop.neds.cascais360;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import nsop.neds.cascais360.Authenticator.AccountGeneral;
import nsop.neds.cascais360.Encrypt.MessageEncryption;
import nsop.neds.cascais360.Manager.Broadcast.AppSignatureHelper;
import nsop.neds.cascais360.Manager.SessionManager;
import nsop.neds.cascais360.Settings.Settings;
import nsop.neds.cascais360.WebApi.ReportManager;
import nsop.neds.cascais360.WebApi.WebApiClient;
import nsop.neds.cascais360.WebApi.WebApiMessages;

import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static nsop.neds.cascais360.Authenticator.AccountGeneral.sServerAuthenticate;

public class LoginActivity extends AppCompatActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();

        setContentView(R.layout.activity_login);
        //Toolbar toolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        /*drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);

        MenuBarManager.SetUserSettings(this, navigationView);*/

        /*TextView title = toolbar.findViewById(R.id.title_quero_ver);
        title.setTextColor(Color.parseColor(Settings.color));
        title.setText(R.string.title_activity_mycascais);*/

        LinearLayout header = findViewById(R.id.login_header);
        header.setBackgroundColor(Color.parseColor(Settings.colors.Gray1));

        Button logon = findViewById(R.id.logon);
        TextView recover = findViewById(R.id.recover);
        TextView register = findViewById(R.id.register);

        TextView terms = findViewById(R.id.terms_conditions);
        TextView privacy = findViewById(R.id.privacy_manifest);

        terms.setTextColor(Color.parseColor(Settings.colors.YearColor));
        privacy.setTextColor(Color.parseColor(Settings.colors.YearColor));
        logon.setBackgroundColor(Color.parseColor(Settings.colors.YearColor));

        logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recover();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mAccountManager = AccountManager.get(getBaseContext());

        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        //MenuBarManager.CallWeather(this, (NavigationView) findViewById(R.id.nav_view));
    }

    private void submit(){
        EditText accountNameField = findViewById(R.id.accountName);
        EditText accountPasswordField = findViewById(R.id.accountPassword);
        String accountName = accountNameField.getText().toString();
        String accountPassword = accountPasswordField.getText().toString();
        login(accountName, accountPassword);
    }

    private void login(String userName, String password){

        String encPass = "";

        try{
            encPass = new MessageEncryption().Encrypt(password, WebApiClient.SITE_KEY);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        String jsonRequest = String.format("{\"Email\":\"%s\", \"Password\":\"%s\"}", userName, encPass);

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Autenticação...");

        progressDialog.show();

        WebApiClient.post(String.format("/%s/%s", WebApiClient.API.WebApiAccount, WebApiClient.METHODS.login), jsonRequest,true, new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String message = WebApiMessages.DecryptMessage(responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String message = WebApiMessages.DecryptMessage(responseString);
                progressDialog.dismiss();

                postSuccess(message);
            }
        });
    }

    private void postSuccess(String json){
        if(ReportManager.isAuthenticated(json)){
            try {
                SessionManager sm = new SessionManager(this);

                sm.setDisplayname(ReportManager.getDisplayname(json));
                sm.setFullName(ReportManager.getFullName(json));
                sm.setDisplaystatus(ReportManager.getDisplayvalidation(json));
                sm.setEmail(ReportManager.getEmail(json));
                sm.setMobileNumber(ReportManager.getMobileNumber(json));
                sm.setAddress(ReportManager.getAddress(json));

                submit(ReportManager.getSSk(json), ReportManager.getUserID(json), ReportManager.getRefreshToken(json));

                sm.setDisclaimers(ReportManager.getDisclaimers(json));
                sm.setAppKeys(ReportManager.getAppKeys(json));
                sm.setFullDisclaimer(ReportManager.getFullDisclaimer(json));

                //intentNavegation();
            }catch (Exception e){
                AccountGeneral.logout(this);
                intentNavegation();
            }
        }else{
            /*AlertDialog.Builder alertMessage = new AlertDialog.Builder(this, R.style.AlertessageDialog);
            alertMessage.setMessage(ReportManager.getReportList(json));
            alertMessage.show();*/
        }
    }

    public void submit(String ssk, String userId, String refreshToken) {
        final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();

        final String accountType = AccountGeneral.ACCOUNT_TYPE; // getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        String authtoken = null;
        Bundle data = new Bundle();

        try {
            authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);

            data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
            data.putString(PARAM_USER_PASS, userPass);
            data.putString("SSK", ssk);
            data.putString("UserId", userId);
            data.putString("RefreshToken", refreshToken);

        } catch (Exception e) {
            data.putString(KEY_ERROR_MESSAGE, e.getMessage());
        }

        final Intent res = new Intent();
        res.putExtras(data);

        if (res.hasExtra(KEY_ERROR_MESSAGE)) {
            Toast.makeText(getBaseContext(), res.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
        } else {
            finishLogin(res);
        }
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        String accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        final Account account = new Account(accountName, accountType);

        if (intent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            mAccountManager.addAccountExplicitly(account, accountPassword, intent.getExtras());
            mAccountManager.setAuthToken(account, authtokenType, authtoken);


        }else{
            mAccountManager.setPassword(account, accountPassword);
        }

        //setResult(RESULT_OK, intent);
        //finish();
        intentNavegation();
    }

    private void recover(){
        SessionManager sm = new SessionManager(this);
        sm.clear();
        sm.setRecover();
        //startActivity(new Intent(LoginActivity.this, RecoverActivity.class));
    }

    private void register(){
        SessionManager sm = new SessionManager(this);
        sm.clear();
        sm.setNewAccount();

        //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    public Intent getParentActivityIntent() {
        Intent parentIntent = getIntent();
        return parentIntent;
    }

    public void intentNavegation(){
        if(getIntent().hasExtra("nid")){
            /*Intent event = new Intent(this, EventActivity.class);
            event.putExtra("nid", getIntent().getIntExtra("nid", 0));
            event.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(event);*/
        }else {
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
