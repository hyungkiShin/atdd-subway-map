package subway.controller.request;

import lombok.Getter;
import subway.common.Discription;
import subway.repository.entity.Line;

import javax.validation.constraints.NotNull;

@Getter
public class LineRequest implements Entityable {

    @Discription("노선 이름")
    @NotNull
    private String name;

    @Discription("노선 색")
    @NotNull
    private String color;

    @Discription("노선 시작역")
    private Long upStationId;

    @Discription("노선 종료역")
    private Long downStationId;

    @Discription("노선 거리")
    private int distance;

    @Override
    public Line toEntity() {
        return Line.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
