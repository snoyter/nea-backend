package com.nea.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(schema = "public", name = "dump_to_file")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DumpToFiles.Key.class)
public class DumpToFiles {
    @Id
    @Column(name = "dump_id")
    private Integer dumpId;
    @Id
    @Column(name = "file_id")
    private Integer fileId;

    @Data
    public static class Key implements Serializable {
        private int dumpId;
        private int fileId;
    }
}
