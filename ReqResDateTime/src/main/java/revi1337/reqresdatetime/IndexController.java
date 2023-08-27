package revi1337.reqresdatetime;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

// 2023-08-25T06:34:20
// 2023-08-25
// 2023-08

@RestController
public class IndexController {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO @ModelAttribute 와 @RequestParam 사용 시
    //  당연하겠지만.. JSON 과 관련없기때문에, @JsonFormat 과 관련이없으며, @DateTimeFormat 을 통해 특정 포맷으로 역직렬화가 수행된다.

    // TODO 결론 :
    //  일반 객체와 @JsonFormat 만 동작. @DateTimeFormat 은 동작하지 않음.
    record RequestWithParam(
            LocalDateTime localDateTime1,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime localDateTime2,

            LocalDate localDate1,
            @DateTimeFormat(pattern = "yyyy-MM") LocalDate localDate2,

            YearMonth yearMonth1,
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth2
    ){}
    @GetMapping("/client-2-server1")
    public void client2ServerWithParameter(RequestWithParam request) {
        System.out.println("request = " + request);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO @RequestBody 사용 시
    //  일반 객체, @JsonFormat, @DateTimeFormat 모두 사용 가능
    //  @JsonFormat, @DateTimeFormat 모두 명시하면 @JsonFormat 가 우선순위를 차지.
    //  @JsonFormat 이 틀리면 @DateTimeFormat 이 맞더라도 역직렬화가 실패.
    //  단, @DateTimeFormat 이 있다면 @DateTimeFormat 의 포맷으로 역직렬화가 진행된다.

    record RequestWithRequestBody(
            LocalDateTime localDateTime1,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime localDateTime2,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime localDateTime3,

            LocalDate localDate1,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate2,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate localDate3,

            YearMonth yearMonth1,
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth2,
            @JsonFormat(pattern = "yyyy-MM") YearMonth yearMonth3
    ){}
    @GetMapping("/client-2-server2")
    public void client2ServerWithJsonBody(@RequestBody RequestWithRequestBody request) {
        System.out.println("request = " + request);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO ResponseBody 를 통해 응답을 내려줄때는 @DateTimeFormat 가 동작하지 않고, @JsonFormat 만 동작한다. (그 이유는 아래와 같음)
    //   --> 일반적으로 ResponseBody 를 사용하게되면 내부적으로 HttpMessageConverter 가 동작하여 그 안에서 JSON 으로 직렬화하는 과정을 거쳐 응답을 내려주게 되는데
    //       그 과정속에서 JSON 으로 직렬화 및 변환을 시켜주는 역할은 Spring 과 직접적으로 관련이없는 Jackson 라이브러리가 진행하게된다.
    //   --> 하지만, Jackson 라이브러리는 Spring 과 직접적으로 관련이 없기 때문에 JSON 으로 변환하는 과정에서
    //       Spring 에서 제공하는 Formatter 어노테이션인 @DateTimeFormat 를 인식하지 못하게된다.
    //   --> 따라서 ResponseBody 에서는 @DateTimeFormat 를 달아주어도 동작하지 않게 된다.
    //   --> @JsonFormat 은 Jackson 라이브러리가 JSON 으로 직렬화하여 변환할때의 Format 을 설정해주는것이기때문에, @DateTimeFormat 과 관계없이 동작하게된다.

    // TODO 결론 :
    //   --> ResponseBody 를 통해 응답을 내줄때는 일반객체와 @JsonFormat 만 사용 가능하다.
    //   --> @DateTimeFormat 은 Spring 이 제공하는 어노테이션이고, @JsonFormat 은 Jackson 라이브러리가 제공하는 어노테이션이며, 이둘은 직접적으로 관계가 없다.
    //   --> JSON 으로 직렬화할때 포맷을설정하는 것이 @JsonFormat 이다.

    record APIResponse(
            LocalDateTime createdAt1,
            @DateTimeFormat(pattern = "yyyy-MM") LocalDateTime createdAt2,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt3,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt4,

            @JsonFormat(pattern = "yyyy-MM") LocalDateTime createdAt5,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDateTime createdAt6,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt7
    ){}
    @GetMapping("/server-2-client")
    public APIResponse server2Client() {
        return new APIResponse(
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
