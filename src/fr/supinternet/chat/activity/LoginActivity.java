package fr.supinternet.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.supinternet.chat.R;

public class LoginActivity extends Activity {
	
	private EditText userNameEdit;
	private EditText passwordEdit;
	
	private Button validateBtn;
	private Button goToCreateBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
		
	}

	private void initViews() {
		
		userNameEdit = (EditText) findViewById(R.id.activity_login_pseudo);
		passwordEdit = (EditText) findViewById(R.id.activity_login_password);
		
		validateBtn = (Button) findViewById(R.id.activity_login_validate);
		goToCreateBtn = (Button) findViewById(R.id.activity_login_go_to_create);
		goToCreateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCreateActvity();
			}
		});
	}

	private void goToCreateActvity() {
		Intent intent = new Intent(this, CreateAccountActivity.class);
		startActivity(intent);
	}
}
