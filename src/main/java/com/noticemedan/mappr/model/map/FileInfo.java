package com.noticemedan.mappr.model.map;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileInfo {
	String fileName;
	String displayName;
	LocalDateTime lastEdited;
	double size; // In bytes
}
