package softeer.be33ma3.utils;

import java.util.Arrays;
import java.util.List;

public class StringParser {    // 구분자 콤마로 문자열 파싱 후 각각의 토큰에서 공백 제거 후 리스트 반환
    public static List<String> stringCommaParsing(String inputString) {
        if (inputString == null || inputString.isBlank()) {
            return List.of();
        }
        return Arrays.stream(inputString.split(","))
                .map(String::strip)
                .toList();
    }
}
