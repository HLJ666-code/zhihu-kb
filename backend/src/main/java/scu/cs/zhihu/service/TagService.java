package scu.cs.zhihu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scu.cs.zhihu.domain.TagNode;
import scu.cs.zhihu.mapper.TagMapper;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagMapper tagMapper;

    public List<TagNode> tree() {
        List<TagNode> all = tagMapper.allTags();
        Map<Integer, TagNode> map = new LinkedHashMap<>();
        for (TagNode n : all) map.put(n.getTagId(), n);
        List<TagNode> roots = new ArrayList<>();
        for (TagNode n : all) {
            if (n.getParentId() == null) {
                roots.add(n);
            } else {
                TagNode parent = map.get(n.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(n);
                }
            }
        }
        return roots;
    }
}
