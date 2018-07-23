package docent.namsanhanok.Setting;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class AdminLoginActivity extends AppCompatActivity {

    ImageButton listBtn;
    Button loginBtn;

    TextInputEditText adminId;
    TextInputEditText adminPassword;

    String admin_id="";
    String admin_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        listBtn = (ImageButton)findViewById(R.id.listBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        adminId = (TextInputEditText)findViewById(R.id.adminId);
        adminPassword = (TextInputEditText)findViewById(R.id.adminPassword);

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin_id = adminId.getText().toString();
                admin_password = adminPassword.getText().toString();

                //관리자 로그인(ID:admin, Password:1111)
                if(admin_id.equals("admin") && admin_password.equals("1111")) {
                    Intent intent = new Intent(AdminLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
