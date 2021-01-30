import java.io.*;
import java.util.*;
/**
 * main
 */
public class main {

    public static void main(String[] args) {
        /**
         * 分割前の時間、分割後の時間、走行距離
         */
        ArrayList<Double[]> timeRecord = new ArrayList<Double[]>(); // 分割された時間.この時間をもとに速度の計算を行う
        ArrayList<Double> distance = new ArrayList<Double>(); // 走行距離
        /**
         * ファイル名が指定されていない場合はコンソールを出力して終了する
         */
        if(args.length != 1) {
            System.out.println("ファイル名を引数に指定してください　");
            System.exit(1);
        }
        /**
         * ファイルを一行ずつ読み込み
         * ・時間, 分, 秒.ミリ秒
         * ・走行距離
         * に分割
         */
        try {
            File file = new File(args[0]);
            BufferedReader stream = new BufferedReader(new FileReader(file));
            String streamLine = stream.readLine();
            while(streamLine != null) {
                String[] data = streamLine.split(" ");
                timeRecord.add(splitTime(data[0]));    
                distance.add(Double.parseDouble(data[1]));
                streamLine = stream.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.print(e);
        } catch (IOException e) {
            System.out.print(e);
        }
        // 時間を要素に分割する
        //for (String timer : time) {
        //    timeRecord.add(splitTime(timer));
        //}

        /**
         * ファイルの中身を表示
         * @todo 実行時には消す
         */        
        //for (int[] timer : timeRecord) {
        //    for (int i = 0; i < timer.length; i++) {
        //        System.out.print(timer[i] + ",");                
        //    }
        //}
        //for (Double dist : distance) {
        //    System.out.print(dist + ",");
        //}

        Double totalDistance = 0.0; // 総走行距離(m)
        Double nowSpeed = 0.0; // 現在の走行速度(km/h)
        int fee = 410; // 料金(yen) (初期値は初乗り料金)
        Boolean midnight = false; // 深夜かどうか
        Boolean lowSpeed = false; // 低速度かどうか

        // 間の距離を計算
        for (int i = 0; i < distance.size() - 1; i++) {
            Double[] firstTime = timeRecord.get(i);
            Double[] lastTime = timeRecord.get(i + 1);

            /**
             * 時間の差を取得
             */
            Double diffT = 0.0;
            for (int j = 0; j < 3; j++) {
                diffT += (lastTime[j] - firstTime[j]) * (3600 / Math.pow(60, j));
            }
            // 小数点第4位以下は計算誤差となるためここで丸める
            diffT = ((double) Math.round(diffT * 1000) / 1000);
            
            /**
             * スピードを計算
             */
            Double speed = calcSpeed(diffT, distance.get(i + 1));
            System.out.println("[" + i + "]" + "diffT:" + diffT + "(s) speed:" + speed + "(km/h)");
            //if(isMidnight(first, last)) {
//
//            }
        }
        /**
         * 時間を計算
         */
        /**
         * タクシーのスピードを計算
         */


    }

    public static double calcSpeed(Double time, Double dist) {
        // 時速を計算する        
        return (((dist / 10.0) * (3600.0 / time)) / 1000.0);
    }

    public static Boolean isMidnight(int first, int last) {
        return (
            ((first % 24) >= 0 && (first % 24) <= 4) || ((first % 24) >= 22 && (first % 24) <= 23)
            &&
            ((last % 24) >= 0 && (last % 24) <= 4) || ((last % 24) >= 22 && (last % 24) <= 23)
            ) ? true : false;
    }
    public static Boolean isLowspeed(Double speed) {
        return (speed <= 10.0) ? true : false;
    }

    private static Double[] splitTime(String in) {
        Double[] out = new Double[4];
        String[] hmsms = in.split(":");
        // すべてDouble[]の配列に格納していく
        out[0] = Double.parseDouble(hmsms[0]); //hour
        out[1] = Double.parseDouble(hmsms[1]); //minute
        out[2] = Double.parseDouble(hmsms[2]); // second and milli second
        return out;
    } 
}