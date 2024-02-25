package softeer.be33ma3.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  //파라미터에서 사용한다는 의미
@Retention(RetentionPolicy.RUNTIME) //리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남도록 설정
public @interface CurrentUser {
}
