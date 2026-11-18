package scu.cs.zhihu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scu.cs.zhihu.common.ApiResponse;
import scu.cs.zhihu.domain.TagNode;
import scu.cs.zhihu.service.TagService;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @GetMapping("/tree")
    public ApiResponse<List<TagNode>> tree() { return ApiResponse.ok(tagService.tree()); }
}
