
/**
 * Taxifee.java
 * @author 398noe
 * @since 2020/01/30
 * @brief タクシーの料金を求めるプログラム
 */

/// import packages
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class taxiFee {

    /**
     * 時速を計算するメソッド
     * 
     * @param time 区間秒
     * @param dist 区間距離
     * @return 時速
     */
    public static double calcIntervalSpeed(Double time, Double dist) {
        return (((dist / 10.0) * (3600.0 / time)) / 1000.0);
    }

    /**
     * 深夜かどうかを判定するメソッド
     * 
     * @param start
     * @param finish
     * @return 深夜であればtrue
     */
    public static Boolean isMidnight(int start, int finish) {
        return (((start % 24) >= 0 && (start % 24) <= 4)
                || ((start % 24) >= 22 && (start % 24) <= 23) && ((finish % 24) >= 0 && (finish % 24) <= 4)
                || ((finish % 24) >= 22 && (finish % 24) <= 23)) ? true : false;
    }

    /**
     * 低速かどうかを判定するメソッド
     * 
     * @param speed 時速(km/h)
     * @return 低速であればtrue
     */
    public static Boolean isLowSpeed(Double speed) {
        return (speed <= 10.0) ? true : false;
    }

    /**
     * 時間の文字列をhh,mm,ss.ms形式に分割するメソッド
     * 
     * @param in 時間の文字列
     * @return hh,mm,ss.ms形式の配列
     */
    private static Double[] splitTime(String in) throws DateFormatException {
        Double[] out = new Double[3];
        String[] hhmmssms = in.split(":");
        try {
            timeFormatCheck(hhmmssms);
            /**
             * 入力形式にエラーが起きていないか確認する
             */
            Double hh = Double.parseDouble(hhmmssms[0]);
            Double mm = Double.parseDouble(hhmmssms[1]);
            Double ssms = Double.parseDouble(hhmmssms[2]);
            out[0] = hh; // hour
            out[1] = mm; // minute
            out[2] = ssms; // second and milli second
        } catch (DateFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return out;
    }

    /**
     * 距離のフォーマットが正しいかどうかをチェックするメソッド
     * 
     * @param in 入力される文字列距離フォーマット
     * @throws DistanceFormatException 距離のフォーマットが違う例外
     */
    public static void distanceFormatCheck(String in) throws DistanceFormatException {
        try {
            String[] num = in.split(Pattern.quote("."));
            /// 形式がxx, xx. の形になっていたら例外を投げる
            if (num.length < 2) {
                throw new DistanceFormatException("距離に小数点以下の値が含まれていません");
            }
            /// 小数点以下が2桁以上となっていたら例外を投げる
            if (num[1].length() >= 2) {
                throw new DistanceFormatException("距離の小数点以下の値が2桁以上です");
            }
            /// 距離の値が99,9を超えるもしくは負の値を取ったら例外を投げる
            if (num[0].length() >= 3 || Double.parseDouble(in) < 0) {
                throw new DistanceFormatException("距離が負の値もしくは99.9より大きいです");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 時刻のフォーマットがあっているかどうか確認するメソッド
     * 
     * @param in 入力される文字列時刻フォーマット
     * @throws DateFormatException 時刻のフォーマットが違う例外
     */
    public static void timeFormatCheck(String[] in) throws DateFormatException {
        try {
            /// 時刻が0～99の間であるかどうかを確認
            if (Integer.parseInt(in[0]) < 0 || in[0].length() >= 3) {
                throw new DateFormatException("時刻が負の値もしくは100を超えています");
            }
            if (Integer.parseInt(in[1]) < 0 || Integer.parseInt(in[1]) >= 60) {
                throw new DateFormatException("分数が負の値もしくは60を超えています");
            }
            /// 秒.ミリ秒の形式チェック
            if ((in[2].split(Pattern.quote("."))).length <= 1) {
                throw new DateFormatException("時間にミリ秒が含まれていません");
            }
            if ((in[2].split(Pattern.quote(".")))[1].length() >= 4) {
                throw new DateFormatException("ミリ秒の値が4桁以上になっています");
            }
            if (Double.parseDouble(in[2]) < 0.0 || Double.parseDouble(in[2]) >= 60.0) {
                throw new DateFormatException("秒数の値が負の値もしくは60を超えています");
            }
        } catch (DateFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * main関数
     * 
     * @param args 入力ファイル名
     */
    public static void main(String[] args) {
        /**
         * 変数定義 時間, 走行距離
         */
        ArrayList<Double[]> timeRecord = new ArrayList<Double[]>(); /// 入力された時間
        ArrayList<Double> distance = new ArrayList<Double>(); /// 走行距離
        /**
         * ファイル名が指定されていない場合はコンソールを出力して終了する
         */
        if (args.length != 1) {
            System.out.println("ファイル名を引数に指定してください");
            System.exit(0);
        }

        /**
         * ファイルを一行ずつ読み込み ・時間, 分, 秒.ミリ秒 ・走行距離 に分割
         */
        try {
            File file = new File(args[0]);
            BufferedReader stream = new BufferedReader(new FileReader(file));
            String streamLine = stream.readLine(); /// 初めの一行を読み込む
            while (streamLine != null) { /// 空行でない限りリストに時間と距離情報を追加していく
                /// 時間, 距離に分割
                String[] data = streamLine.split(" "); /// data[0] = 時間, data[1] = 距離
                /**
                 * 時間のフォーマットが適切かどうかをチェックする. チェックはsplitTimeにて時間を分割している際に行われる
                 */
                Double[] date = splitTime(data[0]);
                /**
                 * 距離の値が正常かどうかをチェックする
                 */
                distanceFormatCheck(data[1]);
                timeRecord.add(date);
                distance.add(Double.parseDouble(data[1]));
                streamLine = stream.readLine();
            }
            stream.close(); /// ファイルストリームを閉じる

            /**
             * 入力行が一行以下もしくは入力距離が0.0でない場合例外を投げる
             */
            if (distance.size() <= 1) {
                throw new LineFormatException("入力行が1行以下です");
            }
            if (distance.get(0) != 0.0) {
                throw new NonZeroFormatException("開始時刻の距離が0.0以外の値です");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        /**
         * 変数定義 変数が多すぎるのでもう少し減らしても問題ないと思う
         */
        double totalDistance = 0.0; /// 総走行距離(m)
        double totalLowSpeedTime = 0.0; /// 総低速時間(秒)
        double intervalBaseRun = 1052.0; /// 初乗りで走れる距離
        double intervalOverRun = 237; /// 料金を加算する距離間隔
        double intervalLowSpeed = 90; /// 低速運賃を加算する時間間隔
        int fee = 410; /// 料金(初期値は初乗り料金)
        int overRunFee = 80; /// 加算運賃(237mごとに80円)
        int lowSpeedFee = 80; /// 低速運賃料金(90秒ごとに80円)

        /// 間の距離を計算
        for (int i = 0; i < distance.size() - 1; i++) {
            /// 開始時刻, 終了時刻(hh,mm,ss.ms形式)
            Double[] startTime = timeRecord.get(i);
            Double[] finishTime = timeRecord.get(i + 1);
            double intervalDistance = distance.get(i + 1);
            /**
             * 区間内の時間の差を計算 intervalTimeとする
             */
            Double intervalTime = 0.0;
            for (int j = 0; j < 3; j++) {
                intervalTime += (finishTime[j] - startTime[j]) * (3600 / Math.pow(60, j));
            }
            /// 小数点第4位以下は計算誤差となるためここで丸める
            intervalTime = ((double) Math.round(intervalTime * 1000) / 1000);
            /// 時間経過が正の値でない場合は例外を投げる
            try {
                if (intervalTime < 0.0) {
                    throw new MinusFormatException("経過時間が負の値です");
                } else if (intervalTime == 0.0) {
                    throw new ZeroFormatException("経過時間が0.0です");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            /**
             * 区間内のスピードを計算
             */
            Double intervalSpeed = calcIntervalSpeed(intervalTime, intervalDistance);
            /**
             * 総走行距離, 運賃を更新する
             */
            Boolean midnight = isMidnight(startTime[0].intValue(), finishTime[0].intValue()); // 深夜かどうか
            Boolean lowSpeed = isLowSpeed(intervalSpeed); // 低速度かどうか

            /**
             * 深夜時間の場合
             */
            if (midnight == true) {
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
            if (lowSpeed == true) {
                /**
                 * 低速時の時間を記録する 深夜の時間帯であれば低速走行時間に1.25倍を補正して加算する
                 */
                if (midnight == true) {
                    totalLowSpeedTime += intervalTime * 1.25;
                } else {
                    totalLowSpeedTime += intervalTime;
                }
            }
        }

        /**
         * 走行距離および料金計算
         */
        try {
            if (totalDistance < 0.1) {
                throw new ZeroFormatException("総走行距離が0.0です");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        /**
         * 初乗り料金で乗れない場合
         */
        if (totalDistance > intervalBaseRun) {
            totalDistance -= intervalBaseRun;
            /*
             * 追加料金を加算する回数
             */
            int overRunCount = ((int) (totalDistance / intervalOverRun)) + 1;
            /**
             * ちょうど237mの場合はoverRunCountの値が1増えてしまうので overRunCountの値を1減らして調整する
             */
            if (totalDistance % 237 == 0) {
                overRunCount -= 1;
            }
            /**
             * 料金を加算
             */
            fee += overRunFee * overRunCount;
        }
        /**
         * 低速運賃を料金に加算
         */
        fee += ((int) (totalLowSpeedTime / intervalLowSpeed)) * lowSpeedFee;
        /**
         * 料金を出力
         */
        System.out.println(fee);
        System.exit(0);
    }
}