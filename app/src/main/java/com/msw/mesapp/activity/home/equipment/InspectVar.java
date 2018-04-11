package com.msw.mesapp.activity.home.equipment;


import java.util.ArrayList;

/**
 * Created by Mr.Meng on 2018/2/2.
 */

public class InspectVar {
    private static InspectVar inst;
    public ArrayList<String> workerRES1 = new ArrayList<>(); //合格
    public ArrayList<String> workerRES2 = new ArrayList<>(); //不合格

    public static InspectVar getInstance() {
        if (inst == null) {
            inst = new InspectVar();
        }
        return inst;
    }
}
