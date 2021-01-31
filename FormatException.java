/**
 * @file FormatException.java
 * @author 398noe
 * @since 2020/01/30
 * @brief 入力書式の例外をまとめたもの
 */

/**
 * FormatException 入力フォーマットが異なる事を示した例外
 */
class FormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public FormatException(String str) {
        super(str);
    }
}

/**
 * DateFormatException 時間のフォーマットが異なることを示した例外
 */
class DateFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DateFormatException(String str) {
        super("時間のフォーマットが間違っています\n" + str);
    }
}

/**
 * DistanceFormatException 距離のフォーマットが異なることを示した例外
 */
class DistanceFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DistanceFormatException(String str) {
        super("距離のフォーマットが違います\n" + str);
    }
}

/**
 * LineFormatException 入力行のフォーマットが異なることを示した例外
 */
class LineFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public LineFormatException(String str) {
        super("入力行は2行以上である必要があります\n" + str);
    }
}

/**
 * NonZeroFormatException 入力数値が0.0以外であることを示した例外
 */
class NonZeroFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public NonZeroFormatException(String str) {
        super("数値は0.0でなければなりません\n" + str);
    }
}

/**
 * ZeroFormatException 数値が0.0であることを示した例外
 */
class ZeroFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public ZeroFormatException(String str) {
        super("数値は0.0であってはいけません\n" + str);
    }
}

/**
 * MinusFormatException 数値が負の値であることを示した例外
 */
class MinusFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public MinusFormatException(String str) {
        super("数値は負の値であってはいけません");
    }
}