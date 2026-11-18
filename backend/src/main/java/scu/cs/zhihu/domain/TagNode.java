package scu.cs.zhihu.domain;

import lombok.Data;
import java.util.List;

@Data
public class TagNode {
    private Integer tagId;
    private String tagName;
    private Integer parentId;
    private List<TagNode> children;
}
