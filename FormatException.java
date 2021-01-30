class FormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public FormatException(String str) {
        super(str);
    }
}

class DateFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DateFormatException(String str) {
        super("時間のフォーマットが間違っています\n" + str);
    }
}

class DistanceFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DistanceFormatException(String str) {
        super("距離のフォーマットが違います\n" + str);
    }
}

class LineFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public LineFormatException(String str) {
        super("入力行は2行以上である必要があります\n" + str);
    }
}

class NonZeroFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public NonZeroFormatException(String str) {
        super("数値は0.0でなければなりません\n" + str);
    }
}

class ZeroFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public ZeroFormatException(String str) {
        super("数値は0.0であってはいけません\n" + str);
    }
}

class MinusFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public MinusFormatException(String str) {
        super("数値は負の値であってはいけません");
    }
}