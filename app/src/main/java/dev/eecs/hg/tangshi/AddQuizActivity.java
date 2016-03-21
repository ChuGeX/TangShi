package dev.eecs.hg.tangshi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class AddQuizActivity extends AppCompatActivity {
    public static final String XIN_SHI_JU_FILE_NAME = "xinshi.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
    }


    public void saveQuiz(View view) {
        EditText shangju = (EditText) findViewById(R.id.shang_text);
        EditText xiaju = (EditText) findViewById(R.id.xia_text);
        String shang_ju = shangju != null ? shangju.getText().toString() : null;
        String xia_ju = xiaju != null ? xiaju.getText().toString() : null;
        if (shang_ju == null || xia_ju == null || shang_ju.length() > 0 || xia_ju.length() > 0) {
            try {
                // 添加新诗句到文件
                PrintStream printStream = new PrintStream(openFileOutput(XIN_SHI_JU_FILE_NAME, MODE_APPEND));
                printStream.println(shang_ju + "-" + xia_ju);

                // 返回
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
