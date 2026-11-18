package scu.cs.zhihu.common;

import lombok.Data;

@Data
public class NoteRequest {
    private Long userId = 1L;
    private Long articleId;
    private String noteText;
}
