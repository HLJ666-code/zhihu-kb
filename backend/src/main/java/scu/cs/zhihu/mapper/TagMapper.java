package scu.cs.zhihu.mapper;

import org.apache.ibatis.annotations.Mapper;
import scu.cs.zhihu.domain.TagNode;
import java.util.List;

@Mapper
public interface TagMapper {
    List<TagNode> allTags();
}
