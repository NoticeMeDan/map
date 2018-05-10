package com.noticemedan.mappr.model.util;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileInfo {
	String name;
	LocalDateTime date;
	String size;
}
