package com.rpg.datamanagement.data;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaveData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private PersonalPlayerInformation playerInformation;
    private Stats stats;
    private String[] abilities;
    private int level;
    private Position position;

}
