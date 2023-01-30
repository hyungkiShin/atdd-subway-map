package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.common.Comment;
import subway.controller.request.LineRequest;
import subway.controller.response.LineResponse;
import subway.util.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/insert-station.sql")
class LineAcceptanceTest {

    private static final String LINE_PATH = "/lines";

    private static final String NAME = "신분당선";
    private static final String NAME2 = "이호선";

    private Map<String, Object> 신분당선;
    private Map<String, Object> 이호선;

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<>();
        신분당선.put("name", NAME);
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", 1);
        신분당선.put("downStationId", 2);
        신분당선.put("distance", 10);

        이호선 = new HashMap<>();
        이호선.put("name", NAME2);
        이호선.put("color", "bg-green-600");
        이호선.put("upStationId", 3);
        이호선.put("downStationId", 4);
        이호선.put("distance", 10);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine()  {
        // given & when
        ExtractableResponse<Response> response = 지하철_라인생성(신분당선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat((String) response.jsonPath().get("name")).isEqualTo(NAME);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLine() {
        // given
        지하철_라인생성(신분당선);
        지하철_라인생성(이호선);

        // when
        final List<String> lineNames = getLineNames();

        // then
        assertThat(lineNames).hasSize(2);
    }

    @Comment("지하철 노선을 생성하는 함수")
    private ExtractableResponse<Response> 지하철_라인생성(final Map<String, Object> line) {

        final LineRequest param = MapHelper.readValue(line, LineRequest.class);

         return RestAssured.given().log().all()
                 .contentType(JSON)
                 .body(param)
                 .when().post(LINE_PATH)
                 .then().log().all()
                 .extract();
    }

    @Comment("지하철 노선 목록의 이름을 반환하는 함수")
    private List<String> getLineNames() {
        return getLines().stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
    }

    @Comment("지하철 노선 목록을 반환하는 함수")
    private List<LineResponse> getLines() {
        return RestAssured
                .given().accept(APPLICATION_JSON_VALUE)
                .when().get(LINE_PATH)
                .then().statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getList("", LineResponse.class);
    }

}
