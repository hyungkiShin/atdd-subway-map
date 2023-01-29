package subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.request.LineRequest;
import subway.util.MapHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/insert-station.sql")
class LineAcceptanceTest {

    private Map<String, Object> map;

    @BeforeEach
    void setup() {
        map = new HashMap<>();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine()  {
        //given
        map.put("name", "신분당선");
        map.put("color", "bg-red-600");
        map.put("upStationId", 1);
        map.put("downStationId", 2);
        map.put("distance", 10);

        // when
        final LineRequest param = MapHelper.readValue(map, LineRequest.class);
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat((String) response.jsonPath().get("name")).isEqualTo("신분당선");
    }
}
