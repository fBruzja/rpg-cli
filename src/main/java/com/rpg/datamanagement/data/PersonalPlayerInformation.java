package com.rpg.datamanagement.data;

import com.rpg.characters.data.Profession;

public record PersonalPlayerInformation(
        String name, String gender, Profession profession
) {

}
