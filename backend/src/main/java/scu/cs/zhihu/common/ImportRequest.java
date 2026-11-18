package scu.cs.zhihu.common;

import lombok.Data;
import scu.cs.zhihu.domain.ImportItem;
import java.util.List;

@Data
public class ImportRequest {
    private String sourceNote;
    private List<ImportItem> items;
}
