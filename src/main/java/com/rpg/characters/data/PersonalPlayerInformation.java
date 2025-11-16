package com.rpg.characters.data;

import java.io.Serializable;

public record PersonalPlayerInformation(
        String name, Profession profession
) implements Serializable {

}
