/*

 * @ (#) WorkMode.java        1.0     11/28/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.enums;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/28/2024
 */
public enum WorkMode {
    ON_SITE("On-site"),REMOTE("Remote"),HYBRID("Hybrid");

    private final String value;

    WorkMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
