package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scu.cs.zhihu.domain.BatchRow;
import scu.cs.zhihu.domain.Overview;
import scu.cs.zhihu.mapper.StatsMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsMapper statsMapper;
    public Overview overview() { return statsMapper.overview(); }
    public List<BatchRow> batches() { return statsMapper.batches(); }
}
