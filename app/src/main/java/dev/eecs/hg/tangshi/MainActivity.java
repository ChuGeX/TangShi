package dev.eecs.hg.tangshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void duiShiClicked(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("filetype", "yuan");
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }

    public void tianJiaClicked(View view) {
        Intent intent = new Intent(this, AddQuizActivity.class);
        startActivity(intent);
    }

    public void cuoJuClicked(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("filetype", "cuo");
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}
