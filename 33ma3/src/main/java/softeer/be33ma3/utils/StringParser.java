package softeer.be33ma3.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static String createTimeParsing(LocalDateTime createTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = createTime.format(formatter);

        if (createTime.getHour() >= 12) {   //오후인 경우 12빼서 보내기
            int hour = createTime.getHour() - 12;
            return formattedTime = "오후 " + (hour < 10 ? "0" + hour : hour)  + ":" + createTime.getMinute();
        }

        return "오전 "  + formattedTime;
    }
}
