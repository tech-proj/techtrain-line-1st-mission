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

        double totalDistance = 0.0; // 総走行距離(m)
        double totalLowSpeedTime = 0.0; // 総低速時間(秒)
        double intervalBaseRun = 1052.0; // 初乗りで走れる距離
        double intervalOverRun = 237; // 料金を加算する距離間隔
        double intervalLowSpeed = 90; // 低速運賃を区切る時間
        int fee = 410; // (初期値は初乗り料金)
        int overRunFee = 80; // 加算運賃(237mごとに80円)
        int lowSpeedFee = 80; // 低速運賃料金(90秒ごとに80円)
        // 間の距離を計算
        for (int i = 0; i < distance.size() - 1; i++) {
            // 開始時刻, 終了時刻(hh,mm,ss.ms形式)
            Double[] startTime = timeRecord.get(i);
            Double[] finishTime = timeRecord.get(i + 1);
            double intervalDistance = distance.get(i + 1);

            /**
             * 区間内の時間の差を計算
             * intervalTimeとする
             */
            Double intervalTime = 0.0;
            for (int j = 0; j < 3; j++) {
                intervalTime += (finishTime[j] - startTime[j]) * (3600 / Math.pow(60, j));
            }
            // 小数点第4位以下は計算誤差となるためここで丸める
            intervalTime = ((double) Math.round(intervalTime * 1000) / 1000);
            
            /**
             * 区間内のスピードを計算
             */
            Double intervalSpeed = calcIntervalSpeed(intervalTime, intervalDistance);
            System.out.println("[" + i + "]" + "intervalTime:" + intervalTime + "(s) intervalSpeed:" + intervalSpeed + "(km/h)");

            /**
             * 総走行距離, 運賃を更新する
             */
            Boolean midnight = isMidnight(startTime[0].intValue(), finishTime[0].intValue()) == true; // 深夜かどうか
            Boolean lowSpeed = isLowSpeed(intervalSpeed); // 低速度かどうか

            /**
             * 深夜時間の場合
             */
            if(midnight == true) {
                /**
                 * 走行距離に1.25をかけて距離を補正する
                 */
                totalDistance += intervalDistance * 1.25;
            } else {
                /**
                 * 通常の時間帯であれば通常の距離を加算する
                 */
                totalDistance += intervalDistance;
            }
            /**
             * 低速の場合
             */
            if(lowSpeed == true) {
                /**
                 * 低速時の時間を記録する
                 * 深夜の時間帯であれば低速走行時間に1.25倍を補正して加算する
                 */
                if(midnight == true) {
                    totalLowSpeedTime += intervalTime * 1.25;
                } else {
                    totalLowSpeedTime += intervalTime;
                }
            }
        }
    
    System.out.println("総走行距離："+ totalDistance + "m");
    System.out.println("総低速走行時間："+ totalLowSpeedTime + "s");
    
    totalDistance = 1289.0;
    /**
     * 初乗り料金で乗れない場合
     */
    if(totalDistance > intervalBaseRun) {
        totalDistance -= intervalBaseRun;
        /**
         * 追加料金を加算する回数
         */
        int overRunCount = ((int) (totalDistance / intervalOverRun)) + 1;
        /**
         * ちょうど237mの場合はoverRunCountの値が1増えてしまうので
         * overRunCountの値を1減らして調整する
         */
        if(totalDistance % 237 == 0) {
            overRunCount -= 1;
        }
        System.out.println("加算回数：" + overRunCount);
        /**
         * 料金を加算
         */
        fee += overRunFee * overRunCount;
    }
    /**
     * 低速運賃を料金に加算
     */
    totalLowSpeedTime = 120.0;
    fee += (int) (totalLowSpeedTime / intervalLowSpeed) * lowSpeedFee;
    /**
     * 料金を出力
     */
    System.out.println(fee);
    System.exit(0);
    }

    /**
     * 時速を計算するメソッド
     * @param time 区間秒
     * @param dist 区間距離
     * @return 時速
     */
    public static double calcIntervalSpeed(Double time, Double dist) {
        return (((dist / 10.0) * (3600.0 / time)) / 1000.0);
    }

    /**
     * 深夜かどうかを判定するメソッド
     * @param start
     * @param finish
     * @return 深夜であればtrue
     */
    public static Boolean isMidnight(int start, int finish) {
        return (
            ((start % 24) >= 0 && (start % 24) <= 4) || ((start % 24) >= 22 && (start % 24) <= 23)
            &&
            ((finish % 24) >= 0 && (finish % 24) <= 4) || ((finish % 24) >= 22 && (finish % 24) <= 23)
            ) ? true : false;
    }
    /**
     * 低速かどうかを判定するメソッド
     * @param speed 時速(km/h)
     * @return 低速であればtrue
     */
    public static Boolean isLowSpeed(Double speed) {
        return (speed <= 10.0) ? true : false;
    }

    /**
     * 時間の文字列をhh,mm,ss.ms形式に分割するメソッド
     * @param in 時間の文字列
     * @return hh,mm,ss.ms形式の配列
     */
    private static Double[] splitTime(String in) {
        Double[] out = new Double[3];
        String[] hmsms = in.split(":");
        out[0] = Double.parseDouble(hmsms[0]); //hour
        out[1] = Double.parseDouble(hmsms[1]); //minute
        out[2] = Double.parseDouble(hmsms[2]); // second and milli second
        return out;
    } 
}