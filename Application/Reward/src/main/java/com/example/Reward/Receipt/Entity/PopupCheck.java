package com.example.Reward.Receipt.Entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
class PopupCheckKey implements Serializable {
    @Column
    private String popup_type;

    @Column
    private long mem_id;
}


@Entity
@Getter
public class PopupCheck {
    @EmbeddedId
    private PopupCheckKey popupCheckKey;
}
