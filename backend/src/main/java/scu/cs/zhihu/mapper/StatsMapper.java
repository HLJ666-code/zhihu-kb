package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import scu.cs.zhihu.domain.BatchRow;
import scu.cs.zhihu.domain.Overview;
import java.util.List;

@Mapper
public interface StatsMapper {
    Overview overview();
    List<BatchRow> batches();
}
