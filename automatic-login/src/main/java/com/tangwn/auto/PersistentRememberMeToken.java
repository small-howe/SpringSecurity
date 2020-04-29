package com.tangwn.auto;

import lombok.Data;

import java.util.Date;

@Data
public class PersistentRememberMeToken {

    private final String username;
    private final String series;
    private final String tokenValue;
    private final Date date;
}
