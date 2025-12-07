package com.usermodule.dto.attachment;

import lombok.Builder;

@Builder
public record AttachmentResponseDTO(Long id,
                                    String fileName,
                                    String filePath,
                                    String category,
                                    String username) {
}
