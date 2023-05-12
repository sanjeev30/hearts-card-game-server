package edu.gmu.cs.hearts.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Integer id;

    private Date startTime;

    private Date endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

}
