package com.yamamz.hroracle.view;

import com.yamamz.hroracle.R;

import java.util.Random;

/**
 * Created by AMRI on 11/30/2016.
 */

public class RamdomImages {

    private static final Random RANDOM = new Random();

    public static int getRandomDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.mb_01;
            case 1:
                return R.drawable.mb_02;
            case 2:
                return R.drawable.mb_03;
            case 3:
                return R.drawable.mb_04;
            case 4:
                return R.drawable.mb_05;
        }
    }
}