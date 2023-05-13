package edu.gmu.cs.hearts.domain.id;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandWinnerId implements Serializable {
    private Integer gameId;
    private Integer moveNo;
}
