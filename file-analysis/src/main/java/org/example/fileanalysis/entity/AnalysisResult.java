package org.example.fileanalysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AnalysisResult {
    @Id
    private Long fileId;

    private int wordCount;
    private int symbolCount;
    private int lineCount;
}
