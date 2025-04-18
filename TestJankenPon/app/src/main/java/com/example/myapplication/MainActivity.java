package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable animationTask;
    int currentIndex = 0;

    MediaPlayer mediaPlayer;

    public enum HandType {
        GU, CHOKI, PA
    }

    HandType currentHand = HandType.GU; // 初期値

    MediaPlayer bgmPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bgmPlayer = MediaPlayer.create(this, R.raw.honwaka);
        bgmPlayer.setLooping(true);
        bgmPlayer.setVolume(0.5f, 0.5f); // 左右の音量を50%に
        bgmPlayer.start();

        Button startButton = findViewById(R.id.startButton);
        Button guButton = findViewById(R.id.guButton);
        Button chokiButton = findViewById(R.id.chokiButton);
        Button parButton = findViewById(R.id.parButton);
        Button restartButton = findViewById(R.id.restartButton);

        ImageView guImageView = findViewById(R.id.guimageView);
        ImageView chokiImageView = findViewById(R.id.chokiimageView);
        ImageView parImageView = findViewById(R.id.parimageView);

        ImageView winImageView = findViewById(R.id.youwinView);
        ImageView loseImageView = findViewById(R.id.youloseView);

        mediaPlayer = MediaPlayer.create(this, R.raw.jankenpon);
        // 再生後にもう一度再生できるようにする（ループしないよう注意）
        mediaPlayer.setOnCompletionListener(mp -> mp.seekTo(0));

        // スタートボタン押下処理
        startButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                // 効果音再生（再初期化不要）
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }

                startButton.setVisibility(View.GONE); // スタートボタンを非表示に

                guButton.setVisibility(View.VISIBLE);    // グーボタン表示
                chokiButton.setVisibility(View.VISIBLE); // チョキボタン表示
                parButton.setVisibility(View.VISIBLE);   // パーボタン表示

                // アニメーション処理
                animationTask = new Runnable() {
                    @Override
                    public void run() {
                        guImageView.setVisibility(View.INVISIBLE);
                        chokiImageView.setVisibility(View.INVISIBLE);
                        parImageView.setVisibility(View.INVISIBLE);

                        switch (currentIndex % 3) {
                            case 0:
                                guImageView.setVisibility(View.VISIBLE);
                                currentHand = HandType.GU;
                                break;
                            case 1:
                                chokiImageView.setVisibility(View.VISIBLE);
                                currentHand = HandType.CHOKI;
                                break;
                            case 2:
                                parImageView.setVisibility(View.VISIBLE);
                                currentHand = HandType.PA;
                                break;
                        }

                        currentIndex++;
                        handler.postDelayed(this, 150);
                    }
                };
                handler.post(animationTask); // ← アニメーション開始ここ！
            }
        });

        // グーボタン押下
        guButton.setOnClickListener(v -> {
            MediaPlayer ponPlayer = MediaPlayer.create(MainActivity.this, R.raw.pon);
            ponPlayer.start();
            ponPlayer.setOnCompletionListener(mp -> {
                mp.release(); // 終了後リリースしてメモリ解放
            });
            handler.removeCallbacks(animationTask); // アニメーション停止
            int hand = currentIndex % 3;

            if (currentHand == HandType.GU) {
                // グーが出ていた
                // ここに勝敗判定や表示処理を入れる
                MediaPlayer makePlayer = MediaPlayer.create(MainActivity.this, R.raw.aikode);
                makePlayer.start();
                makePlayer.setOnCompletionListener(mp -> {
                    mp.release(); // 終了後リリースしてメモリ解放
                });
                // あいこの時だけアニメーション再開を0.5秒遅らせる
                handler.postDelayed(animationTask, 500); // 500ミリ秒＝0.5秒後に再開
            } else if ( currentHand == HandType.PA) {
                // パーが出ていた
                MediaPlayer makePlayer = MediaPlayer.create(MainActivity.this, R.raw.make);
                makePlayer.start();
                makePlayer.setOnCompletionListener(mp -> {
                    mp.release(); // 終了後リリースしてメモリ解放
                });
                loseImageView.setVisibility(View.VISIBLE); // 負け画像表示
                restartButton.setVisibility(View.VISIBLE);

                guButton.setVisibility(View.INVISIBLE);
                chokiButton.setVisibility(View.INVISIBLE);
                parButton.setVisibility(View.INVISIBLE);
            } else {
                //　チョキが出ていた
                MediaPlayer makePlayer = MediaPlayer.create(MainActivity.this, R.raw.kati);
                makePlayer.start();
                makePlayer.setOnCompletionListener(mp -> {
                    mp.release(); // 終了後リリースしてメモリ解放
                });
                winImageView.setVisibility(View.VISIBLE);    // 勝ち画像表示
                restartButton.setVisibility(View.VISIBLE);

                guButton.setVisibility(View.INVISIBLE);
                chokiButton.setVisibility(View.INVISIBLE);
                parButton.setVisibility(View.INVISIBLE);
            }
        });

        restartButton.setOnClickListener(v -> {
            winImageView.setVisibility(View.INVISIBLE);
            loseImageView.setVisibility(View.INVISIBLE);
            guButton.setVisibility(View.INVISIBLE);
            chokiButton.setVisibility(View.INVISIBLE);
            parButton.setVisibility(View.INVISIBLE);

            guImageView.setVisibility(View.INVISIBLE);
            chokiImageView.setVisibility(View.INVISIBLE);
            parImageView.setVisibility(View.INVISIBLE);

            restartButton.setVisibility(View.INVISIBLE);

            startButton.setVisibility(View.VISIBLE);
        });
    }
}