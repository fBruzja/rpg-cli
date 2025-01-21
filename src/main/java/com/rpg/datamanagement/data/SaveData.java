package com.rpg.datamanagement.data;

import com.rpg.characters.data.PersonalPlayerInformation;
import com.rpg.characters.data.Position;
import com.rpg.characters.data.Stats;
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
    private static final long serialVersionUID = -1L;

    private  transient PersonalPlayerInformation playerInformation;
    private transient Stats stats;
    private String[] abilities;
    private int level;
    private transient Position position;

}
