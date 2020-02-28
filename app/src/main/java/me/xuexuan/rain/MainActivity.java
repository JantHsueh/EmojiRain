package me.xuexuan.rain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.xuexuan.RainViewGroup;

public class MainActivity extends AppCompatActivity {

    private RainViewGroup rainView;
    private Button btnStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rainView = findViewById(R.id.testView);
        btnStar = findViewById(R.id.btn_sun);


        //屏幕中最多显示item的数量
        rainView.setAmount(50);
        //设置下落的次数。在保持密度不变的情况下，设置下落数量。例如：数量 = 50，下落次数 = 3，总共数量150。
        // rainView.setTimes(2);
        //设置无效循环
        rainView.setTimes(RainViewGroup.INFINITE);
        rainView.start();

        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.stop();
                rainView.setImgResId(R.mipmap.sun);
                rainView.start();
            }
        });

    }
}
