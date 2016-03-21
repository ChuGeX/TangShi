package dev.eecs.hg.tangshi;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class QuizActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private HashMap<String, String> dictionary;
    private ArrayList<String> xia;
    private ArrayAdapter<String> adapter;
    private final int SCORE_MEI_JU = 5;
    private MediaPlayer gotit;
    private MediaPlayer loseit;
    private ImageView state = null;
    private int answer_index;
    public static final String CUO_SHI_JU_FILE_NAME = "cuoshi.txt";
    private String shang;
    private int dui_count = 0;
    private int zong_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        xia = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                R.layout.shi_list_item,
                R.id.shi_item_text,
                xia);
        ListView shi_list = (ListView) findViewById(R.id.shi_list);
        shi_list.setAdapter(adapter);
        shi_list.setOnItemClickListener(this);

        readShiFile();

    }



    private void readShiFile() {
        dictionary = new HashMap<>();

        // 读诗三百
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.shi300));
        readShiJu(scanner);

        // 读新诗句
        try {
            Scanner xinShiScanner = new Scanner(openFileInput(AddQuizActivity.XIN_SHI_JU_FILE_NAME));
            readShiJu(xinShiScanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void readShiJu(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String shiju = scanner.nextLine();
            if (shiju.contains("-")) {
                String[] shidui = shiju.split("-");
                dictionary.put(shidui[0], shidui[1]);
            }
        }

        getQuiz();
    }

    private void getQuiz() {
        ArrayList<String> quizKey = new ArrayList(dictionary.keySet());
        Collections.shuffle(quizKey);
        answer_index = new Random().nextInt(4);
        shang = quizKey.get(answer_index);
        TextView shang_ju = (TextView) findViewById(R.id.shang_ju);
        shang_ju.setText(shang);

        xia.clear();
        String xiaju;
        for (int i = 0; i < 4; i++) {
            xiaju = dictionary.get(quizKey.get(i));
            xia.add(xiaju);
        }
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        zong_count++;
        final ListView shi_list = (ListView) findViewById(R.id.shi_list);
        shi_list.setEnabled(false);
        View right_answer = shi_list.getChildAt(answer_index);
        state = (ImageView) right_answer.findViewById(R.id.dui_cuo);
        state.setImageResource(android.R.drawable.star_big_on);
        if (answer_index == position) {
            Toast.makeText(this, "对了", Toast.LENGTH_SHORT).show();
            gotit.start();
            dui_count++;
        } else {
            Toast.makeText(this, "错了", Toast.LENGTH_SHORT).show();
            loseit.start();
            try {
                PrintStream printStream = new PrintStream(openFileOutput(CUO_SHI_JU_FILE_NAME, MODE_APPEND));
                printStream.println(shang + "-" + dictionary.get(shang));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        setScore();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                state.setImageResource(android.R.drawable.star_big_off);
                shi_list.setEnabled(true);
                getQuiz();
            }
        }, 2000);
    }

    private void setScore() {
        int score = (2 * dui_count - zong_count) * SCORE_MEI_JU;
        TextView score_text = (TextView) findViewById(R.id.score_text);
        score_text.setText("分数: " + score);
        TextView count_text = (TextView) findViewById(R.id.count_text);
        count_text.setText(dui_count + "/" + zong_count);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("duicount", dui_count);
        outState.putInt("zongcount", zong_count);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dui_count = savedInstanceState.getInt("duicount");
        zong_count = savedInstanceState.getInt("zongcount");
        setScore();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gotit = MediaPlayer.create(this, R.raw.dui);
        loseit = MediaPlayer.create(this, R.raw.cuo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gotit.release();
        gotit = null;
        loseit.release();
        loseit = null;
    }
}
