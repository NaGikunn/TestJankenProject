package com.example.myapplication;

public abstract class HandBase
{
    /*自分の手の名前を取得する*/
    public abstract String GetName();

    /*じゃんけんの判定を取得する*/
    public abstract boolean beats(HandBase other);

//    public enum HandType {
//        GU, CHOKI, PA
//    }
//
//    HandType currentHand = MainActivity.HandType.GU; // 初期値
}
