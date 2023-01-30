package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.request.LineRequest;
import subway.controller.response.LineResponse;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LineController {

    private final LineService lineService;

    @PostMapping("/lines")
    private ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        final LineResponse lineStation = lineService.createLine(lineRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineStation.getId())).body(lineStation);
    }

    @GetMapping("/lines")
    private ResponseEntity<List<LineResponse>> getLines() {
        final List<LineResponse> lines = lineService.getLines();
        return ResponseEntity.ok(lines);
    }
}
