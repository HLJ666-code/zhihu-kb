package scu.cs.zhihu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scu.cs.zhihu.common.ApiResponse;
import scu.cs.zhihu.domain.BatchRow;
import scu.cs.zhihu.domain.Overview;
import scu.cs.zhihu.service.StatsService;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    @GetMapping("/overview")
    public ApiResponse<Overview> overview() { return ApiResponse.ok(statsService.overview()); }
    @GetMapping("/batches")
    public ApiResponse<List<BatchRow>> batches() { return ApiResponse.ok(statsService.batches()); }
}
