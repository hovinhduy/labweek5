/*

 * @ (#) SkillLevel.java        1.0     11/3/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.enums;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/3/2024
 */

import lombok.Getter;

@Getter
public enum SkillLevel {
    MASTER(5), BEGINER(1), ADVANCED(3), PROFESSIONAL(4), IMTERMEDIATE(2);

    private final int value;

    SkillLevel(int value) {
        this.value = value;
    }
}
