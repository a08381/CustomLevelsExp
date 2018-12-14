package io.github.windmourn.CustomLevelsExp.entry;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TotalExp")
public class TotalExp {

    @Getter
    @Setter
    @Id
    public int id;

    @Getter
    @Setter
    @Column(name = "playername", nullable = false, unique = true)
    public String playerName;

    @Getter
    @Setter
    @Column(name = "totalexp", nullable = false)
    public long totalExp;
}
