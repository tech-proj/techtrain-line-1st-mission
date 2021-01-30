class FormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public FormatException(String str) {
        super(str);
    }
}

class DateFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DateFormatException(String str) {
        super("日付のフォーマットが間違っています\n" + str);
    }
}

class DoubleFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DoubleFormatException(String str) {
        super("数値のフォーマットが間違っています\n" + str);
    }
}

class DistanceFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public DistanceFormatException(String str) {
        super("距離の値・フォーマットが違います\n" + str);
    }
}

class LineNumberFormatException extends FormatException {
    private static final long serialVersionUID = 1L;

    public LineNumberFormatException(String str) {
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