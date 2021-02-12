package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    val handler = Handler(Looper.getMainLooper())
    var timeValue = 0           //秒数カウンター

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //変数に代入
        val timeText  = findViewById<TextView>(R.id.timeText)
        val startButton = findViewById<Button>(R.id.start)
        val stopButton = findViewById<Button>(R.id.stop)
        val restButton = findViewById<Button>(R.id.reset)

        val runnable = object : Runnable {
            override fun run() {
                timeValue++        //秒数プラス１
                //?.letでnullではない時だけ更新
                timeToText(timeValue)?.let {
                    //itはlet内ではtimeToText(timeValue)として使える
                    timeText.text = it
                }
                handler.postDelayed(this, 1000) //一秒後にpostへ渡す
            }
        }
        //start
        startButton.setOnClickListener {
            //キューの登録
            handler.post(runnable)
        }

        //stop
        stopButton.setOnClickListener {
            //キューのキャンセル
            handler.removeCallbacks(runnable)
        }

        //rest
        restButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            timeValue = 0
            timeToText()?.let {
                timeText.text = it
            }
        }

    }
    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            //0未満場合
            null
        } else if (time == 0) {
            //0だった場合
            "00:00:00"
        } else {
            //0より大きかった場合
            val h = time / 3600 //時間
            val m = time % 3600 / 60 //分数
            val s = time % 60 //秒数
            //%1は1つ目　$02dは二桁の数値を0で穴埋めした文字列
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}